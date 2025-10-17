package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.IZone;
import model.players.IPlayer;
import model.players.Player;
import model.utils.EnumColors;
import model.versions.risikockassic.managers.MapManagerRisikoNew;
import model.versions.risikockassic.managers.TankManager;

public class TankManagerTest {

    private TankManager tankManager;
    private MapManagerRisikoNew mapManager;
    private List<IPlayer> players;
    
    @BeforeEach
    void setUp() {
    	// Reset di entrambi i manager prima di ogni test
    	MapManagerRisikoNew.getInstance().resetGame();
    	mapManager = MapManagerRisikoNew.getInstance();
    	
        tankManager = new  TankManager();
        
        players = new ArrayList<>();
        players.add(new Player("Player1", EnumColors.RED));
        players.add(new Player("Player2", EnumColors.BLUE));
        players.add(new Player("Player3", EnumColors.GREEN));
    }
    
    @Test
    void testInitializeGame_TooFewPlayers() {
        List<IPlayer> tooFewPlayers = new ArrayList<>();
        tooFewPlayers.add(new Player("Player1", EnumColors.RED));
        tooFewPlayers.add(new Player("Player2", EnumColors.BLUE));
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tankManager.initializeGame(tooFewPlayers),
            "Should throw exception for too few players"
        );
        assertEquals("Number of players must be between 3 and 6", exception.getMessage());
    }
    
    @Test
    void testGetPlayerTanks_NotReady() {
        IPlayer player = new Player("TestPlayer", EnumColors.RED);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> tankManager.getPlayerTanks(player),
            "Should throw exception when not ready"
        );
        assertEquals("TokenManager must be initialized before use.", exception.getMessage());
    }
    
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
            assertEquals("TokenManager is already initialized.", exception.getMessage());
        }
    }
    
    @Test
    void testTanksAssigned() {
    	
    }
}