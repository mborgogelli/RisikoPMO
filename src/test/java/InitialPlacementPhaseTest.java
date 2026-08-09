import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.MediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.turn.GameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.InitialPlacementPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InitialPlacementPhaseTest {

    private final GameFactoryRisikoNew gf = new GameFactoryRisikoNew();
    private List<IPlayer> players;
    private IMapManagerRisikoNew mapManager;
    private ITankManager tankManager;
    private IMediatorRisikoNew mediator;
    private IGameCoordinatorRisikoNew gameCoordinator;

    private <T> T resolveManager(Class<T> managerType) {
        return gf.getManagers().stream()
            .filter(managerType::isInstance)
            .map(managerType::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Manager of type " + managerType.getName() + " not found"));
    }

    private boolean haveRemainingTanks(IMediatorRisikoNew mediator) {
        return this.players.stream().anyMatch(p -> mediator.getPlayerTank(p) > 0);
    }

    @BeforeEach
    public void setUp() {
        players = List.of(
            new Player("A"),
            new Player("B"),
            new Player("C"),
            new Player("D")
        );

        this.mapManager = resolveManager(IMapManagerRisikoNew.class);
        this.tankManager = resolveManager(ITankManager.class);
        this.mediator = (IMediatorRisikoNew) gf.getMediator();
    }

    @Test
    @DisplayName("Integration: Deploy exactly 3 tanks when available")
    void testPlayPhaseDeploysThreeWhenAvailable() {

        this.mapManager.initializeGame(players);
        this.tankManager.initializeGame(players);

        for (IPlayer player : players) {
            System.out.println(player.getName() + ": " + this.mapManager.getZonesOwnedBy(player).size() + " zones");
            System.out.println(player.getName() + ":  " + this.tankManager.getPlayerToken(player) + " tanks.");
            System.out.println(this.mapManager.getZonesOwnedBy(player).size() + this.tankManager.getPlayerToken(player));
        }
        InitialPlacementPhase phase = new InitialPlacementPhase(this.mediator, new GameCoordinatorRisikoNew());

        while (haveRemainingTanks(mediator)) {
            for (IPlayer player : this.players) {
                if (mediator.getPlayerTank(player) > 0) {
                    phase.playPhase(player);
                }
            }
        }
    }

   /* @Test
    @DisplayName("Integration: Deploy less than 3 tanks when fewer available")
    void testPlayPhaseDeploysRemainingWhenLessThanThree() {

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
    }*/

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
            new Player("Unit1"),
            new Player("Unit2"),
            new Player("Unit3"),
            new Player("Unit4")
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
