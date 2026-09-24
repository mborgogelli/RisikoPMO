package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.DeployCommand;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.PlayerStateRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialPlacementPhase;
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
    private IMapManagerRisikoNew mapManager;
    private ITankManager tankManager;
    private IMediatorRisikoNew mediator;
    private IGameCoordinatorRisikoNew gameCoordinator;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.initManagers();
    }

    private void initManagers() {
        this.players = super.getPlayers();
        this.mapManager = getManager(IMapManagerRisikoNew.class);
        this.tankManager = getManager(ITankManager.class);
        this.mediator = super.getMediator();
        this.gameCoordinator = super.getGameCoordinator();
    }

    @Test
    @DisplayName("Verify deployment request data")
    void testPlayPhaseDeploysAllTanks() {


        // Rimuove tutti i tank dai giocatori e assegna 3 tank al giocatore ultimo
        for (IPlayer player : this.players) {
            this.tankManager.removeTank(player, this.mediator.getPlayerTank(player));
        }
        this.tankManager.assignTank(this.players.getLast(), 3);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> phase.playPhase(this.players.getFirst()));
        assertEquals("Not enough tanks to deploy.", ex.getMessage());

        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getLast()).getState();

        // Verifica che i dati della request siano coerenti con i dati attesi
        assertNotNull(request);
        assertEquals(3, request.tokenToDeploy().get(ERisikoNewToken.TANK));
        assertEquals(this.players.getLast().getName(), request.playerName());
        assertEquals(this.players.getLast().getColor(), request.playerColor());
        assertTrue(this.mediator.getTerritoriesOwnedBy(this.players.getLast()).containsAll(request.deployableZones()));

    }

    @Test
    @DisplayName("Verify command execution with invalid territories")
    void testDeployCommandWithWrongZones() {

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getFirst()).getState();
        DeployCommand command = new DeployCommand(Map.of("Zone A", 2, "Zone B", 1));

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

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(this.players.getFirst()).getState();
        DeployCommand command = new DeployCommand(Map.of(request.deployableZones().getFirst(), 2,
                request.deployableZones().getLast(), 2));

        List<String> deployableZones = request.deployableZones();
        List<String> deployedChoice = command.deployment().keySet().stream().toList();
        int tanksDeployed = command.deployment().values().stream().mapToInt(Integer::intValue).sum();

        // Verifica che i dati della request siano coerenti con i dati attesi
        assertFalse(deployableZones.isEmpty());
        assertTrue(deployableZones.containsAll(deployedChoice));
        assertFalse(3 == tanksDeployed);

        assertFalse(phase.isValidCommand(command));
        assertThrows(IllegalArgumentException.class, () -> phase.handleCommand(command));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> phase.handleCommand(command));
        assertEquals("Not valid command.", ex.getMessage());

    }

    @Test
    @DisplayName("Verify Player state after command execution")
    void testPlayerStateAfterCommandExecution() {

        IPlayer currentPlayer = this.players.getFirst();
        String playerName = currentPlayer.getName();
        EColors playerColor = currentPlayer.getColor();
        int tanksOwned = this.mediator.getPlayerTank(currentPlayer);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator);
        DeployRequestRisikoNewDTO request = (DeployRequestRisikoNewDTO) phase.playPhase(currentPlayer).getState();

        List<String> deployableZones = request.deployableZones();

        // Situazione attuale sulle zone che verranno rinforzate
        String firstZone = deployableZones.getFirst();
        int currentTanksOnFirstZone = this.mediator.getZoneTank(firstZone);
        String lastZone = deployableZones.getLast();
        int currentTanksOnLastZone = this.mediator.getZoneTank(lastZone);

        DeployCommand command = new DeployCommand(Map.of(request.deployableZones().getFirst(), 1,
                                                         request.deployableZones().getLast(), 2));

        // Verifica che il comando sia valido
        assertTrue(phase.isValidCommand(command));

        PlayerStateRisikoNewDTO playerState = (PlayerStateRisikoNewDTO) phase.handleCommand(command).getState();

        //Nuovo stato del giocatore
        assertEquals(playerState.playerName(), playerName);
        assertEquals(playerState.playerColor(), playerColor);
        assertEquals(this.mediator.getPlayerTank(currentPlayer), tanksOwned - 3);
        assertEquals(this.mediator.getZoneTank(firstZone), currentTanksOnFirstZone + 1);
        assertEquals(this.mediator.getZoneTank(lastZone), currentTanksOnLastZone + 2);
    }
}
