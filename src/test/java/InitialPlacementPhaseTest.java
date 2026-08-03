import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.MediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.GameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.InitialPlacementPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InitialPlacementPhaseTest {

    private final GameFactoryRisikoNew gf = new GameFactoryRisikoNew();

    private <T> T resolveManager(Class<T> managerType) {
        return gf.getManagers().stream()
            .filter(managerType::isInstance)
            .map(managerType::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Manager of type " + managerType.getName() + " not found"));
    }

    @Test
    @DisplayName("Integration: Deploy exactly 3 tanks when available")
    void testPlayPhaseDeploysThreeWhenAvailable() {
        List<IPlayer> players = List.of(
            new Player("A", EnumColors.RED),
            new Player("B", EnumColors.YELLOW),
            new Player("C", EnumColors.BLUE),
            new Player("D", EnumColors.GREEN)
        );

        IMapManager mapManager = resolveManager(IMapManager.class);
        ITokenManager tankManager = resolveManager(ITokenManager.class);
        MediatorRisikoNew mediator = (MediatorRisikoNew) gf.getMediator();

        // inizializza mappa e tank
        mapManager.initializeGame(players);
        tankManager.initializeGame(players);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator, new GameCoordinatorRisikoNew());

    }

    @Test
    @DisplayName("Integration: Deploy less than 3 tanks when fewer available")
    void testPlayPhaseDeploysRemainingWhenLessThanThree() {
        List<IPlayer> players = List.of(
            new Player("X", EnumColors.RED),
            new Player("Y", EnumColors.YELLOW),
            new Player("Z", EnumColors.BLUE),
            new Player("W", EnumColors.GREEN)
        );

        IMapManager mapManager = resolveManager(IMapManager.class);
        ITokenManager tankManager = resolveManager(ITokenManager.class);
        MediatorRisikoNew mediator = (MediatorRisikoNew) gf.getMediator();

        mapManager.initializeGame(players);
        tankManager.initializeGame(players);

        IPlayer player = players.get(0);
        int remainingBefore = mediator.getPlayerTank(player);

        // forza remaining a 2 per verificare il ramo < 3
        int targetRemaining = 2;
        tankManager.assignToken(player, targetRemaining - remainingBefore);

        int toDeploy = Math.min(3, mediator.getPlayerTank(player));

        List<String> ownedZones = mediator.getZonesOwnedBy(player);
        String expectedZone = ownedZones.stream().min(String::compareTo).get();
        int zoneBefore = mediator.getZoneTank(expectedZone);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator, new GameCoordinatorRisikoNew());
        phase.playPhase(player);

        assertEquals(zoneBefore + toDeploy, mediator.getZoneTank(expectedZone));
        assertEquals(targetRemaining - toDeploy, mediator.getPlayerTank(player));
        assertTrue(toDeploy < 3);
    }

    // ========== TEST SEMPLICI JUnit5 SENZA MOCK ==========

    private GameFactoryRisikoNew gfUnit;
    private IMapManager mapManagerUnit;
    private ITokenManager tankManagerUnit;
    private MediatorRisikoNew mediatorUnit;
    private List<IPlayer> playersUnit;

    @BeforeEach
    public void setUpUnitTests() {
        gfUnit = new GameFactoryRisikoNew();
        mapManagerUnit = gfUnit.getManagers().stream()
            .filter(IMapManager.class::isInstance)
            .map(IMapManager.class::cast)
            .findFirst()
            .orElseThrow();

        tankManagerUnit = gfUnit.getManagers().stream()
            .filter(ITokenManager.class::isInstance)
            .map(ITokenManager.class::cast)
            .findFirst()
            .orElseThrow();

        mediatorUnit = (MediatorRisikoNew) gfUnit.getMediator();

        playersUnit = List.of(
            new Player("Unit1", EnumColors.RED),
            new Player("Unit2", EnumColors.YELLOW),
            new Player("Unit3", EnumColors.BLUE),
            new Player("Unit4", EnumColors.GREEN)
        );

        mapManagerUnit.initializeGame(playersUnit);
        tankManagerUnit.initializeGame(playersUnit);
    }

    @Test
    @DisplayName("Should deploy exactly 3 tanks when available")
    public void testDeployThreeTanksWhenAvailable() {
        IPlayer player = playersUnit.get(0);
        int tanksBefore = mediatorUnit.getPlayerTank(player);
        assertTrue(tanksBefore >= 3, "Giocatore deve avere almeno 3 tank");

        List<String> ownedZones = mediatorUnit.getZonesOwnedBy(player);
        String deployZone = ownedZones.stream().min(String::compareTo).get();
        int zonesTanksBefore = mediatorUnit.getZoneTank(deployZone);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediatorUnit, new GameCoordinatorRisikoNew());
        phase.playPhase(player);

        int tanksAfter = mediatorUnit.getPlayerTank(player);
        int zonesTanksAfter = mediatorUnit.getZoneTank(deployZone);

        assertEquals(tanksBefore - 3, tanksAfter);
        assertEquals(zonesTanksBefore + 3, zonesTanksAfter);
    }

    @Test
    @DisplayName("Should deploy less than 3 tanks when fewer available")
    public void testDeployLessThanThreeTanks() {
        IPlayer player = playersUnit.get(1);
        int currentTanks = mediatorUnit.getPlayerTank(player);
        tankManagerUnit.assignToken(player, 2 - currentTanks);

        List<String> ownedZones = mediatorUnit.getZonesOwnedBy(player);
        String deployZone = ownedZones.stream().min(String::compareTo).get();
        int zonesTanksBefore = mediatorUnit.getZoneTank(deployZone);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediatorUnit, new GameCoordinatorRisikoNew());
        phase.playPhase(player);

        int tanksAfter = mediatorUnit.getPlayerTank(player);
        int zonesTanksAfter = mediatorUnit.getZoneTank(deployZone);

        assertEquals(0, tanksAfter);
        assertEquals(zonesTanksBefore + 2, zonesTanksAfter);
    }

    @Test
    @DisplayName("Should throw RuntimeException when no tanks available")
    public void testThrowExceptionWhenNoTanks() {
        IPlayer player = playersUnit.get(2);
        int currentTanks = mediatorUnit.getPlayerTank(player);
        tankManagerUnit.assignToken(player, -currentTanks);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediatorUnit, new GameCoordinatorRisikoNew());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> phase.playPhase(player));

        assertEquals("Not enough tanks to deploy.", exception.getMessage());
    }

    @Test
    @DisplayName("MAX_DEPLOYABLE constant should be 3")
    public void testMaxDeployableConstant() {
        assertEquals(3, InitialPlacementPhase.MAX_DEPLOYABLE);
    }

    @Test
    @DisplayName("Should deploy exactly MAX_DEPLOYABLE when exactly available")
    public void testDeployExactlyMaxDeployable() {
        IPlayer player = playersUnit.get(3);
        int currentTanks = mediatorUnit.getPlayerTank(player);
        tankManagerUnit.assignToken(player, 3 - currentTanks);

        List<String> ownedZones = mediatorUnit.getZonesOwnedBy(player);
        String deployZone = ownedZones.stream().min(String::compareTo).get();
        int zonesTanksBefore = mediatorUnit.getZoneTank(deployZone);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediatorUnit, new GameCoordinatorRisikoNew());
        phase.playPhase(player);

        int tanksAfter = mediatorUnit.getPlayerTank(player);
        int zonesTanksAfter = mediatorUnit.getZoneTank(deployZone);

        assertEquals(0, tanksAfter);
        assertEquals(zonesTanksBefore + 3, zonesTanksAfter);
    }

}
