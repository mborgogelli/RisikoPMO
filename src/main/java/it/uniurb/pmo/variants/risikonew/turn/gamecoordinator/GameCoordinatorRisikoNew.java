package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.dto.*;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployChoiceRisikoNewDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    private volatile IGameEvent<? extends IGameState> lastGameEvent;
    private volatile GameSnapshotDTO latestGameSnapshot;
    private List<IGameCommandReceiver> commandReceivers;

    public GameCoordinatorRisikoNew() {
        this.commandReceivers = new ArrayList<>();
    }

    @Override
    public void onGameEvent(IGameEvent<? extends IGameState> event) {
        if (event.getState() instanceof GameSnapshotDTO snapshot) {
            this.latestGameSnapshot = snapshot;
        }
        this.lastGameEvent = event;
    }

    /**
     * Restituisce l'ultimo aggiornamento ricevuto dal TurnManager.
     * Sara' usato dal controller per esporre lo stato della partita.
     */
    @Override
    public Optional<IGameEvent<? extends IGameState>> getLastGameEvent() {
        return Optional.ofNullable(this.lastGameEvent);
    }

    @Override
    public Optional<GameSnapshotDTO> getLatestGameSnapshot() {
        return Optional.ofNullable(this.latestGameSnapshot);
    }


    @Override
    public IDeployChoiceDTO sendDeployRequest(IDeployRequestDTO request) {
        if (request == null || request.deployableZones() == null || request.deployableZones().isEmpty()) {
            return new DeployChoiceRisikoNewDTO(Map.of());
        }
        String targetZone = request.deployableZones().stream().sorted().findFirst().orElse(null);
        return new DeployChoiceRisikoNewDTO(Map.of());
    }

    @Override
    public Optional<IAttackChoiceDTO> sendAttackRequest(IAttackRequestDTO request) {
        return Optional.empty();
    }

    @Override
    public FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request) {
        return new FortifyChoiceDTO(null, null, 0);
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

    }
}
