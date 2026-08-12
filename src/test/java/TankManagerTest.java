
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import java.util.List;

import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import org.junit.jupiter.api.Test;

public class TankManagerTest extends RisikoNewTestSetup {

    @Test
    void testInitializeGame_TooFewPlayers() {
        // Usa una factory fresca per evitare che il tokenManager sia già inizializzato
        GameFactoryRisikoNew freshFactory = new GameFactoryRisikoNew();
        ITankManager freshTokenManager = freshFactory.getManagers().stream()
            .filter(ITankManager.class::isInstance)
            .map(ITankManager.class::cast)
            .findFirst()
            .orElseThrow();
        IMapManagerRisikoNew freshMapManager = freshFactory.getManagers().stream()
            .filter(IMapManagerRisikoNew.class::isInstance)
            .map(IMapManagerRisikoNew.class::cast)
            .findFirst()
            .orElseThrow();
        List<IPlayer> twoPlayers = List.of(new Player("P1"), new Player("P2"));
        freshMapManager.initializeGame(twoPlayers);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> freshTokenManager.initializeGame(twoPlayers),
            "Should throw exception for too few players"
        );
        assertEquals("Invalid number of players: 2", exception.getMessage());
    }
    
   @Test
    void testInitializeGame_AlreadyInitialized() {
        // tokenManager è già inizializzato dal setup
        if (tokenManager.isReady()) {
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> tokenManager.initializeGame(players),
                "Should throw exception when already initialized"
            );
            assertEquals("TankManager is already initialized.", exception.getMessage());
        }
    }
    
    @Test
    void testTanksAssignment() {
        var myAvailableTanks = tokenManager.getPlayerTank(players.get(0));
        var myTerritoriesCount = mapManager.getTerritoriesOwnedBy(players.get(0)).size();
        var myTerritories = mapManager.getTerritoriesOwnedBy(players.get(0));
        var totalDeployed = tokenManager.getTotalDeployed(players.get(0));
        var deployedPerZone = tokenManager.getDeployedPerZone(players.get(0));
        
        assertEquals(myAvailableTanks + myTerritoriesCount, 30); // 30 tank totali per 4 giocatori
        assertEquals(myTerritoriesCount, totalDeployed);
        assertTrue(deployedPerZone.keySet().stream()
                                .allMatch(zone -> myTerritories.contains(zone)));
    }
}
