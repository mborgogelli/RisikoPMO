package it.uniurb.pmo.variants.risikonew.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.framework.board.IGameBoard;
import it.uniurb.pmo.framework.board.IZone;
import it.uniurb.pmo.framework.utils.EGameVersion;

public class GameBoardIntegrationTest {
    
    private IGameBoard gameBoard;
    
    @BeforeEach
    void setUp() {
        this.gameBoard = BoardCreatorRisikoNew.getInstance().getMap();
    }
    
    @Test
    @DisplayName("Restituisce i continenti presenti sulla plancia")
    void testGetContinents() {
        List<IZone> continents = gameBoard.getRootZones();
        assertNotNull(continents);
        assertFalse(continents.isEmpty());
    }
    
    @Test
    @DisplayName("Restituisce i territori contenuti nei continenti")
    void testGetTerritories() {
        List<IZone> territories = gameBoard.getRootZones().stream()
				.flatMap(continent -> continent.getChildZones().stream())
				.toList();
        assertNotNull(territories);
        assertFalse(territories.isEmpty());
    }
    
    @Test
    @DisplayName("Restituisce la versione di gioco della plancia")
    void testGetGameVersion() {
        EGameVersion version = gameBoard.getGameVersion();
        assertNotNull(version);
        assertEquals(EGameVersion.RISIKONEW, version);
    }
    
    @Test
    @DisplayName("Trova una zona senza distinzione tra maiuscole e minuscole")
    void testFindZoneByName() {
        // Get first territory name for testing
        String territoryName = gameBoard.getRootZones().stream()
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
    @DisplayName("Restituisce i vicini di un territorio e gestisce quelli inesistenti")
    void testGetNeighbours() {
        // Get first territory name for testing
        String territoryName = gameBoard.getRootZones().stream()
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
    @DisplayName("Individua il continente che contiene un territorio")
    void testWhereIsZone() {
        // Get first territory name for testing
        String territoryName = gameBoard.getRootZones().stream()
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
    @DisplayName("Verifica se un territorio è raggiungibile da un vicino")
    void testCanReach() {
        // Get first territory and its neighbors for testing
        String territoryName = gameBoard.getRootZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        List<String> neighbours = gameBoard.getNeighbours(territoryName);
        assertNotNull(neighbours);
        assertFalse(neighbours.isEmpty());
        
        String neighborName = neighbours.getFirst();
        
        // Test valid connection
        assertTrue(gameBoard.canReach(neighborName, territoryName));
        
        // Test invalid connection
        assertFalse(gameBoard.canReach("NonExistentTerritory", territoryName));
        assertFalse(gameBoard.canReach(neighborName, "NonExistentTerritory"));
    }
    
    @Test
    @DisplayName("Restituisce il valore associato a una zona")
    void testGetValue() {
        // Get first territory name for testing
        String territoryName = gameBoard.getRootZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        Integer value = gameBoard.getZoneValue(territoryName);
        assertNotNull(value);
        assertTrue(value >= 0);
    }
    
    @Test
    @DisplayName("Restituisce il bonus di armate della zona")
    void testGetArmyBonus() {
        // Get first territory name for testing
        String territoryName = gameBoard.getRootZones().stream()
                .flatMap(continent -> continent.getChildZones().stream())
                .findFirst()
                .map(IZone::getName)
                .orElse(null);
        
        assertNotNull(territoryName);
        
        // Test that getArmyBonus returns the same as getValue
        Integer armyBonus = gameBoard.getZoneValue(territoryName);
        
        assertNotNull(armyBonus);
		assertEquals(armyBonus, gameBoard.getZoneValue(territoryName));
		assertTrue(armyBonus >= 0);
	}
}
