import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.MediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.InitialPlacementPhase;

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

        IPlayer player = players.get(0);
        int remainingBefore = mediator.getPlayerTank(player);
        int toDeploy = Math.min(3, remainingBefore);

        // la zona scelta è la minima lessicograficamente tra quelle possedute (i tank iniziali sono tutti uguali)
        List<String> ownedZones = mediator.getZonesOwnedBy(player);
        String expectedZone = ownedZones.stream().min(String::compareTo).get();
        int zoneBefore = mediator.getZoneTank(expectedZone);

        InitialPlacementPhase phase = new InitialPlacementPhase();
        phase.playPhase(player, mediator);

        assertEquals(zoneBefore + toDeploy, mediator.getZoneTank(expectedZone));
        assertEquals(remainingBefore - toDeploy, mediator.getPlayerTank(player));
    }

    @Test
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

        InitialPlacementPhase phase = new InitialPlacementPhase();
        phase.playPhase(player, mediator);

        assertEquals(zoneBefore + toDeploy, mediator.getZoneTank(expectedZone));
        assertEquals(targetRemaining - toDeploy, mediator.getPlayerTank(player));
        assertTrue(toDeploy < 3);
    }

}

