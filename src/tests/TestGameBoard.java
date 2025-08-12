package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.IGameBoard;
import model.board.IZone;
import model.management.MapManager;
import model.utils.GameVersion;

import java.util.List;

public class TestGameBoard {
    
    private IGameBoard gameBoard;
    
    @BeforeEach
    void setUp() {
    	MapManager mapManager = MapManager.getInstance();
        mapManager.requestGameMap(GameVersion.RISIKOCLASSIC);
        this.gameBoard = mapManager.getGameBoard();
    }
    
    @Test
    void testGetContinents() {
        List<IZone> continents = gameBoard.getZones();
        assertNotNull(continents);
        assertFalse(continents.isEmpty());
    }
    
    @Test
    void testGetTerritories() {
        List<IZone> territories = gameBoard.getZones().stream()
				.flatMap(continent -> continent.getChildZones().stream())
				.toList();
        assertNotNull(territories);
        assertFalse(territories.isEmpty());
    }
    
}
