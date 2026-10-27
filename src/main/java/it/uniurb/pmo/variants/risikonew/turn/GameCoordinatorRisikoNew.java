package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.controller.game.CurrentStateMapper;
import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.variants.risikonew.turn.snapshot.IRisikoGameSnapshot;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    private volatile IGameEvent lastGameEvent;
    private volatile IGameSnapshot latestGameSnapshot;
    private final CurrentStateMapper currentStateMapper;
    private List<IGameCommandReceiver> commandReceivers;

    public GameCoordinatorRisikoNew() {
        this.commandReceivers = new CopyOnWriteArrayList<>();
        this.currentStateMapper = new CurrentStateMapper();
    }

    @Override
    public void onGameEvent(IGameEvent event) {
        this.latestGameSnapshot = event.getSnapshot();
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
        Optional<ICombatResultDTO> combatResult = Optional.empty();
        if (this.latestGameSnapshot instanceof IRisikoGameSnapshot risikoSnapshot) {
            combatResult = risikoSnapshot.lastCombatResult();
        }
        return combatResult;
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
}
