package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.command.PassCommand;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.phase.request.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IDefendRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IMovementRequest;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;
import it.uniurb.pmo.framework.utils.CurrentStateMapper;
import it.uniurb.pmo.variants.risikonew.turn.command.AttackCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DefendCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.MoveCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.snapshot.IRisikoGameSnapshot;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameCoordinatorRisikoNew implements IGameCoordinator {

    private volatile IGameEvent lastGameEvent;
    private volatile IGameSnapshot latestGameSnapshot;
    private volatile ICombatResultDTO lastCombatResult;
    private final CurrentStateMapper currentStateMapper;
    private final DeployCommandMapperRisikoNew deployCommandMapper;
    private List<IGameCommandReceiver> commandReceivers;

    public GameCoordinatorRisikoNew() {
        this.commandReceivers = new CopyOnWriteArrayList<>();
        this.currentStateMapper = new CurrentStateMapper();
        this.deployCommandMapper = new DeployCommandMapperRisikoNew();
    }

    @Override
    public void onGameEvent(IGameEvent event) {
        this.latestGameSnapshot = event.getSnapshot();
        if (event.getSnapshot() instanceof IRisikoGameSnapshot risikoSnapshot) {
            risikoSnapshot.combatResult().ifPresent(result -> this.lastCombatResult = result);
        }
        this.lastGameEvent = event;
    }

    /**
     * Restituisce l'ultimo aggiornamento ricevuto dal TurnManager.
     * Sara' usato dal controller per esporre lo stato della partita.
     */
    @Override
    public Optional<IGameEvent> lastGameEvent() {
        return Optional.ofNullable(this.lastGameEvent);
    }

    @Override
    public Optional<IGameSnapshot> getLatestGameSnapshot() {
        return Optional.ofNullable(this.latestGameSnapshot);
    }

    @Override
    public Optional<CurrentStateDTO> currentState(String roomId) {
        return this.lastGameEvent().map(event -> this.currentStateMapper.toCurrentState(
                roomId, event, this.getCombatResult().orElse(null)));
    }

    @Override
    public Optional<ICombatResultDTO> getCombatResult() {
        return Optional.ofNullable(this.lastCombatResult);
    }

    @Override
    public void setCommandReceiver(IGameCommandReceiver receiver) {
        this.commandReceivers.add(receiver);
    }

    @Override
    public void removeCommandReceiver(IGameCommandReceiver receiver) {
        this.commandReceivers.remove(receiver);
    }

    @Override
    public void submitCommand(IGameCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Game command is required");
        }
        if (this.commandReceivers.size() != 1) {
            throw new IllegalStateException("Exactly one game command receiver must be registered");
        }

        IGameCommandReceiver receiver = this.commandReceivers.getFirst();
        if (receiver.isValidCommand(command)) {
            receiver.handleCommand(command);
        } else {
            throw new IllegalArgumentException("Not valid command.");
        }
    }

    @Override
    public void submitDeploy(String playerName, String tokenType, Map<String, Integer> deployment) {
        IGameEvent currentEvent = this.lastGameEvent()
                .orElseThrow(() -> new IllegalStateException("Game state is not available."));
        if (!(currentEvent.getSnapshot().pendingAction().orElse(null) instanceof IDeployRequestDTO)) {
            throw new IllegalArgumentException("La fase corrente non richiede un piazzamento.");
        }
        boolean matchesCurrentPlayer = currentEvent.getSnapshot().currentPlayerName()
                .map(name -> Objects.equals(name, playerName))
                .orElse(false);
        if (!matchesCurrentPlayer) {
            throw new IllegalStateException("Non è il turno del giocatore indicato.");
        }
        this.submitCommand(this.deployCommandMapper.toCommand(tokenType, deployment));
    }

    @Override
    public ITokenType resolveTokenType(String tokenTypeName) {
        return this.deployCommandMapper.toTokenType(tokenTypeName);
    }

    @Override
    public void submitAttack(String playerName, String attackerZone, String defenderZone,
                             ITokenType tokenType, int tokenAmount) {
        this.validateRisikoNewTokenType(tokenType);
        IGameSnapshot snapshot = this.currentSnapshot();
        if (!this.isAttackPending(snapshot)) {
            throw new IllegalArgumentException("La fase corrente non richiede un attacco.");
        }
        boolean matchesCurrentPlayer = snapshot.currentPlayerName()
                .map(name -> Objects.equals(name, playerName)).orElse(false);
        if (!matchesCurrentPlayer) {
            throw new IllegalStateException("Non è il turno del giocatore indicato.");
        }
        this.submitCommand(new AttackCommandRisikoNew(attackerZone, defenderZone, tokenAmount));
    }

    @Override
    public void submitDefend(String playerName, ITokenType tokenType, int tokenAmount) {
        this.validateRisikoNewTokenType(tokenType);
        IGameSnapshot snapshot = this.currentSnapshot();
        IDefendRequestDTO request = this.requirePendingAction(snapshot, IDefendRequestDTO.class,
                "La fase corrente non richiede una difesa.");
        if (!Objects.equals(request.defenderName(), playerName)) {
            throw new IllegalStateException("Non è il turno del giocatore indicato.");
        }
        this.submitCommand(new DefendCommandRisikoNew(tokenAmount));
    }

    @Override
    public void submitPass(String playerName) {
        IGameSnapshot snapshot = this.currentSnapshot();
        boolean canPass = this.isAttackPending(snapshot)
                || snapshot.pendingAction().orElse(null) instanceof IMovementRequest;
        if (!canPass) {
            throw new IllegalArgumentException("La fase corrente non consente di saltare l'azione.");
        }
        boolean matchesCurrentPlayer = snapshot.currentPlayerName()
                .map(name -> Objects.equals(name, playerName)).orElse(false);
        if (!matchesCurrentPlayer) {
            throw new IllegalStateException("Non è il turno del giocatore indicato.");
        }
        this.submitCommand(new PassCommand());
    }

    @Override
    public void submitMovement(String playerName, ITokenType tokenType, String source,
                               String destination, int tokenAmount) {
        this.validateRisikoNewTokenType(tokenType);
        IGameSnapshot snapshot = this.currentSnapshot();
        if (!(snapshot.pendingAction().orElse(null) instanceof IMovementRequest)) {
            throw new IllegalArgumentException("La fase corrente non richiede uno spostamento.");
        }
        boolean matchesCurrentPlayer = snapshot.currentPlayerName()
                .map(name -> Objects.equals(name, playerName)).orElse(false);
        if (!matchesCurrentPlayer) {
            throw new IllegalStateException("Non è il turno del giocatore indicato.");
        }
        this.submitCommand(new MoveCommandRisikoNew(source, destination, tokenAmount));
    }

    private boolean isAttackPending(IGameSnapshot snapshot) {
        return snapshot.pendingAction().orElse(null) instanceof IAttackRequestDTO
                || snapshot.phaseId().filter("RISIKONEW_ATTACK"::equals).isPresent()
                && this.getCombatResult().filter(result -> !result.possibleTargets().isEmpty()).isPresent();
    }

    private void validateRisikoNewTokenType(ITokenType tokenType) {
        if (tokenType != ERisikoNewToken.TANK) {
            throw new IllegalArgumentException("Token type is not valid for combat.");
        }
    }

    private IGameSnapshot currentSnapshot() {
        return this.lastGameEvent().map(IGameEvent::getSnapshot)
                .orElseThrow(() -> new IllegalStateException("Game state is not available."));
    }

    private <T> T requirePendingAction(IGameSnapshot snapshot, Class<T> actionType, String message) {
        Object action = snapshot.pendingAction().orElse(null);
        if (!actionType.isInstance(action)) {
            throw new IllegalArgumentException(message);
        }
        return actionType.cast(action);
    }
}
