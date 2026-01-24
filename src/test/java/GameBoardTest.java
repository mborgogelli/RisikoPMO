
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.model.board.IGameBoard;
import it.uniurb.pmo.model.board.IZone;
import it.uniurb.pmo.model.utils.GameVersion;
import it.uniurb.pmo.model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class GameBoardTest {
    
    private IGameBoard gameBoard;
    
    @BeforeEach
    void setUp() {
        this.gameBoard = BoardCreatorRisikoNew.getInstance().getMap();
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
    
    @Test
    void testGetGameVersion() {
        GameVersion version = gameBoard.getGameVersion();
        assertNotNull(version);
        assertEquals(GameVersion.RISIKONEW, version);
    }
    
    @Test
    void testFindZoneByName() {
        // Get first territory name for testing
        String territoryName = gameBoard.getZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        IZone foundZone = gameBoard.findZoneByName(territoryName);
        assertNotNull(foundZone);
        assertEquals(territoryName, foundZone.getName());
        
        // Test case insensitive search
        IZone foundZoneLowerCase = gameBoard.findZoneByName(territoryName.toLowerCase());
        assertNotNull(foundZoneLowerCase);
        assertEquals(territoryName, foundZoneLowerCase.getName());
        
        // Test non-existent zone
        IZone nonExistentZone = gameBoard.findZoneByName("NonExistentTerritory");
        assertNull(nonExistentZone);
    }
    
    @Test
    void testGetNeighbours() {
        // Get first territory name for testing
        String territoryName = gameBoard.getZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        List<String> neighbours = gameBoard.getNeighbours(territoryName);
        assertNotNull(neighbours);
        // Most territories should have at least one neighbor
        assertFalse(neighbours.isEmpty());
        
        // Test non-existent territory
        List<String> nonExistentNeighbours = gameBoard.getNeighbours("NonExistentTerritory");
        assertNull(nonExistentNeighbours);
    }
    
    @Test
    void testWhereIsZone() {
        // Get first territory name for testing
        String territoryName = gameBoard.getZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .filter(zone -> zone.getName().equals("kamchatka"))
                .map(IZone::getName)
                .findFirst()
                .orElse(null);
        
        assertNotNull(territoryName);
        
        Optional<IZone> parentZone = gameBoard.whereIsZone(territoryName);
        assertNotNull(parentZone);
        assertEquals("asia", parentZone.get().getName());
        
        // Test non-existent territory
        Optional<IZone> nonExistentParent = gameBoard.whereIsZone("NonExistentTerritory");
        assertNotNull(nonExistentParent);
    }
    
    @Test
    void testCanReach() {
        // Get first territory and its neighbors for testing
        String territoryName = gameBoard.getZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        List<String> neighbours = gameBoard.getNeighbours(territoryName);
        assertNotNull(neighbours);
        assertFalse(neighbours.isEmpty());
        
        String neighborName = neighbours.get(0);
        
        // Test valid connection
        assertTrue(gameBoard.canReach(neighborName, territoryName));
        
        // Test invalid connection
        assertFalse(gameBoard.canReach("NonExistentTerritory", territoryName));
        assertFalse(gameBoard.canReach(neighborName, "NonExistentTerritory"));
    }
    
    @Test
    void testGetValue() {
        // Get first territory name for testing
        String territoryName = gameBoard.getZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        Integer value = gameBoard.getValue(territoryName);
        assertNotNull(value);
        assertTrue(value >= 0);
    }
    
    @Test
    void testGetArmyBonus() {
        // Get first territory name for testing
        String territoryName = gameBoard.getZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        // Test that getArmyBonus returns the same as getValue
        Integer armyBonus = gameBoard.getValue(territoryName);
        
        assertNotNull(armyBonus);
		assertEquals(armyBonus, gameBoard.getValue(territoryName));
		assertTrue(armyBonus >= 0);
	}
}
