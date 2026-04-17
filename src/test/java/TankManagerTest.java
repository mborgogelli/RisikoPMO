
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.model.management.interfaces.IGameFactory;
import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.IMapManager;
import it.uniurb.pmo.model.management.interfaces.ITokenManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.versions.risikockassic.GameFactoryRisikoNew;

public class TankManagerTest {

    private IGameFactory gf = new GameFactoryRisikoNew();
	private List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
											new Player("Player2", EnumColors.YELLOW),
											new Player("Player3", EnumColors.BLUE));
  private ITokenManager tankManager;
  private IMapManager mapManager;

    @BeforeEach
    void setUp() {
        mapManager = this.resolveManager(IMapManager.class);
        tankManager = this.resolveManager(ITokenManager.class);

    }

      private <T extends IManager> T resolveManager(Class<T> managerType) {
        return gf.getManagers().stream()
            .filter(managerType::isInstance)
            .map(managerType::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Manager of type " + managerType.getName() + " not found"));
      }

    @Test
    void testInitializeGame_TooFewPlayers() {
        List<IPlayer> tooFewPlayers = new ArrayList<>(this.players);
        tooFewPlayers.removeLast();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tankManager.initializeGame(tooFewPlayers),
            "Should throw exception for too few players"
        );
        assertEquals("Invalid number of players: 2", exception.getMessage());
    }
    /*
    @Test
    void testGetPlayerTanks_NotReady() {
        // TO DO
        IPlayer player = new Player("TestPlayer", EnumColors.RED);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> tankManager.getPlayerToken(player),
            "Should throw exception when not ready"
        );
        assertEquals("AbstractTokenManager must be initialized before use.", exception.getMessage());
    }*/
    
   @Test
    void testInitializeGame_AlreadyInitialized() {
    	
    	mapManager.initializeGame(players);
        tankManager.initializeGame(players);
        
        // Se tankManager era già pronto, dovrebbe lanciare un'eccezione quando si prova a inizializzarlo di nuovo
        if (tankManager.isReady()) {
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> tankManager.initializeGame(players),
                "Should throw exception when already initialized"
            );
            assertEquals("TankManager is already initialized.", exception.getMessage());
        }
    }
    
    @Test
    void testTanksAssignment() {
    	
    	mapManager.initializeGame(players);
        tankManager.initializeGame(players);
        
            var myAvailableTanks = tankManager.getPlayerToken(players.get(0));
            var myTerritoriesCount = mapManager.getZonesOwnedBy(players.get(0)).size();
            var myTerritories = mapManager.getZonesOwnedBy(players.get(0));
        var totalDeployed = tankManager.getTotalDeployed(players.get(0));
        var deployedPerZone = tankManager.getDeployedPerZone(players.get(0));
        
        
        assertEquals(myAvailableTanks + myTerritoriesCount, 35);
        assertEquals(myTerritoriesCount, totalDeployed);
        assertTrue(deployedPerZone.keySet().stream()
        							.allMatch(zone -> myTerritories.contains(zone)));
        
    }
}