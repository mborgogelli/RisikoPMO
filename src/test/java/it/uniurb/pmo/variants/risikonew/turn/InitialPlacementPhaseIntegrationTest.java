package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.framework.turn.snapshot.IPlayerSnapshot;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.variants.risikonew.turn.command.DeployCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase.PhaseInitialPlacement;
import it.uniurb.pmo.variants.risikonew.turn.phase.request.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InitialPlacementPhaseIntegrationTest extends RisikoNewTestSetup {

    private List<IPlayer> players;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.players = super.getPlayers();
    }

    @Test
    @DisplayName("Verify deployment request data")
    void testPlayPhaseDeploysAllTanks() {


        // Rimuove tutti i tank dai giocatori e assegna 3 tank al giocatore ultimo
        for (IPlayer player : this.players) {
            tankManager.removeTank(player, this.mediator.getPlayerTank(player));
        }
            tankManager.assignTank(this.players.getLast(), 3);

        PhaseInitialPlacement phase = new PhaseInitialPlacement(mediator);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> phase.playPhase(this.players.getFirst()));
        assertEquals("Not enough tanks to deploy.", ex.getMessage());

        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getLast()).phasePayload().orElseThrow();

        // Verifica che i dati della request siano coerenti con i dati attesi
        assertNotNull(request);
        assertEquals(3, request.tokenToDeploy().get(ERisikoNewToken.TANK));
        assertTrue(this.mediator.getTerritoriesOwnedBy(this.players.getLast()).containsAll(request.deployableZones()));

    }

    @Test
    @DisplayName("Verify command execution with invalid territories")
    void testDeployCommandWithWrongZones() {

        PhaseInitialPlacement phase = new PhaseInitialPlacement(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getFirst()).phasePayload().orElseThrow();
        DeployCommandRisikoNew command = new DeployCommandRisikoNew(ERisikoNewToken.TANK,
                Map.of("Zone A", 2, "Zone B", 1));

        List<String> deployableZones = request.deployableZones();
        List<String> deployedChoice = command.deployment().keySet().stream().toList();
        int tanksDeployed = command.deployment().values().stream().mapToInt(Integer::intValue).sum();

        // Verifica che i dati della request siano coerenti con i dati attesi
        assertFalse(deployableZones.isEmpty());
        assertFalse(deployableZones.containsAll(deployedChoice));
        assertEquals(3, tanksDeployed);

        assertFalse(phase.isValidCommand(command));
        assertThrows(IllegalArgumentException.class, () -> phase.handleCommand(command));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> phase.handleCommand(command));
        assertEquals("Not valid command.", ex.getMessage());

    }

    @Test
    @DisplayName("Verify command execution with invalid tanks number")
    void testDeployCommandWithWrongTanksNumber() {

        PhaseInitialPlacement phase = new PhaseInitialPlacement(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getFirst()).phasePayload().orElseThrow();
        DeployCommandRisikoNew command = new DeployCommandRisikoNew(ERisikoNewToken.TANK, Map.of(
                request.deployableZones().getFirst(), 2,
                request.deployableZones().getLast(), 2));

        List<String> deployableZones = request.deployableZones();
        List<String> deployedChoice = command.deployment().keySet().stream().toList();
        int tanksDeployed = command.deployment().values().stream().mapToInt(Integer::intValue).sum();

        // Verifica che i dati della request siano coerenti con i dati attesi
        assertFalse(deployableZones.isEmpty());
        assertTrue(deployableZones.containsAll(deployedChoice));
        assertNotEquals(3, tanksDeployed);

        assertFalse(phase.isValidCommand(command));
        assertThrows(IllegalArgumentException.class, () -> phase.handleCommand(command));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> phase.handleCommand(command));
        assertEquals("Not valid command.", ex.getMessage());

    }

    @Test
    @DisplayName("Reject a deployment command without a token type")
    void testDeployCommandWithoutTokenType() {
        PhaseInitialPlacement phase = new PhaseInitialPlacement(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getFirst()).phasePayload().orElseThrow();
        DeployCommandRisikoNew command = new DeployCommandRisikoNew(null,
                Map.of(request.deployableZones().getFirst(), 1));

        assertFalse(phase.isValidCommand(command));
    }

    @Test
    @DisplayName("Verify Player phasePayload after command execution")
    void testPlayerStateAfterCommandExecution() {

        IPlayer currentPlayer = this.players.getFirst();
        String playerName = currentPlayer.getName();
        EColors playerColor = currentPlayer.getColor();
        int tanksOwned = this.mediator.getPlayerTank(currentPlayer);

        PhaseInitialPlacement phase = new PhaseInitialPlacement(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(currentPlayer).phasePayload().orElseThrow();

        List<String> deployableZones = request.deployableZones();

        // Situazione attuale sulle zone che verranno rinforzate
        String firstZone = deployableZones.getFirst();
        int currentTanksOnFirstZone = this.mediator.getZoneTank(firstZone);
        String lastZone = deployableZones.getLast();
        int currentTanksOnLastZone = this.mediator.getZoneTank(lastZone);

        DeployCommandRisikoNew command = new DeployCommandRisikoNew(ERisikoNewToken.TANK, Map.of(
                request.deployableZones().getFirst(), 1,
                request.deployableZones().getLast(), 2));

        assertEquals(ERisikoNewToken.TANK, command.tokenType());

        // Verifica che il comando sia valido
        assertTrue(phase.isValidCommand(command));
        IPhaseResult result = phase.handleCommand(command);

        IPlayerSnapshot playerState = turnManager.gameSnapshot().get(playerName);

        //Nuovo stato del giocatore
        assertEquals(playerState.playerName(), playerName);
        assertEquals(playerState.playerColor(), playerColor);
        assertEquals(this.mediator.getPlayerTank(currentPlayer), tanksOwned - 3);
        assertEquals(this.mediator.getZoneTank(firstZone), currentTanksOnFirstZone + 1);
        assertEquals(this.mediator.getZoneTank(lastZone), currentTanksOnLastZone + 2);
    }
}
