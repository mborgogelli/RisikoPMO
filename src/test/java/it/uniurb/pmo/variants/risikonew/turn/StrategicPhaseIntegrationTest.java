package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.PassCommand;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.turn.command.MoveCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase.PhaseStrategic;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StrategicPhaseIntegrationTest extends RisikoNewTestSetup {

    @BeforeEach
    void setUp() {
        setUpRisikoNew();
    }

    @Test
    @DisplayName("Il comando Pass non modifica il numero di tanks dei territori")
    void passCompletesPhaseWithoutChangingTankCounts() {
        IPlayer player = players.getFirst();
        prepareMovement(player);
        PhaseStrategic phase = new PhaseStrategic(mediator);
        Map<String, Integer> initialTanks = tankCounts(player);
        phase.playPhase(player);

        IPhaseResult<?> result = phase.handleCommand(new PassCommand());

        assertTrue(result.isCompleted());
        assertTrue(result.phasePayload().isEmpty());
        assertEquals(initialTanks, tankCounts(player));
    }

    @Test
    @DisplayName("Un comando di Spostamento valido trasferisce i tanks richiesti e completa la fase")
    void validMovementTransfersRequestedTanksAndCompletesPhase() {
        IPlayer player = players.getFirst();
        List<String> zones = prepareMovement(player);
        String source = zones.getFirst();
        String destination = zones.getLast();
        PhaseStrategic phase = new PhaseStrategic(mediator);
        phase.playPhase(player);
        int sourceTanksBefore = mediator.getZoneTank(source);
        int destinationTanksBefore = mediator.getZoneTank(destination);

        IPhaseResult<?> result = phase.handleCommand(new MoveCommandRisikoNew(source, destination, 1));

        assertTrue(result.isCompleted());
        assertTrue(result.phasePayload().isEmpty());
        assertEquals(sourceTanksBefore - 1, mediator.getZoneTank(source));
        assertEquals(destinationTanksBefore + 1, mediator.getZoneTank(destination));
    }

    @Test
     @DisplayName("Un comando di Spostamento con un numero di tanks non positivo viene rifiutato")
    void movementWithNonPositiveTankCountIsRejected() {
        IPlayer player = players.getFirst();
        List<String> zones = prepareMovement(player);
        String source = zones.getFirst();
        String destination = zones.getLast();
        PhaseStrategic phase = new PhaseStrategic(mediator);
        phase.playPhase(player);

        assertFalse(phase.isValidCommand(new MoveCommandRisikoNew(source, destination, 0)));
        assertFalse(phase.isValidCommand(new MoveCommandRisikoNew(source, destination, -1)));
    }

    @Test
     @DisplayName("Un comando dopo il completamento della fase viene rifiutato")
    void commandAfterPhaseCompletionIsRejected() {
        IPlayer player = players.getFirst();
        List<String> zones = prepareMovement(player);
        String source = zones.getFirst();
        String destination = zones.getLast();
        PhaseStrategic phase = new PhaseStrategic(mediator);
        phase.playPhase(player);
        phase.handleCommand(new PassCommand());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> phase.handleCommand(new MoveCommandRisikoNew(source, destination, 1)));

        assertEquals("Invalid command.", exception.getMessage());
    }

    private List<String> prepareMovement(IPlayer player) {
        List<String> ownedTerritories = mediator.getTerritoriesOwnedBy(player);
        String source = ownedTerritories.getFirst();
        String destination = mediator.getNeighboursOf(source).getFirst();
        mediator.changeZoneOwnership(destination, player);
        tankManager.addTanksToZone(source, 2);
        return List.of(source, destination);
    }

    private Map<String, Integer> tankCounts(IPlayer player) {
        List<String> territories = mediator.getTerritoriesOwnedBy(player);
        return territories.stream().collect(Collectors.toMap(
                territory -> territory, mediator::getZoneTank));
    }
}
