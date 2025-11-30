package tests;

import model.players.IPlayer;
import model.players.Player;
import model.utils.EnumColors;
import model.versions.risikockassic.managers.MapManagerRisikoNew;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MapManagerRisikoNewTest extends MapManagerRisikoNew {

    private MapManagerRisikoNew mapManager = new MapManagerRisikoNew();
    private List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
											new Player("Player2", EnumColors.YELLOW),
											new Player("Player3", EnumColors.BLUE));

    @BeforeEach
    void initialize() {
        this.mapManager.initializeGame(players);
    }

    @Test
	void testMapInitialization() {
		var allZones = mapManager.getAllTerritories();
		var totalTerritories = allZones.size();
		var territoriesPerPlayer = totalTerritories / players.size();

		for (IPlayer player : players) {
			var ownedZones = mapManager.getZonesOwnedBy(player);
			
			assertNotNull(ownedZones, "Owned zones should not be null.");
			assertEquals(territoriesPerPlayer, ownedZones.size(),
					"Each player should own an equal number of territories.");
		}
	}
    
    @Test
    void testGetAllTerritories() {
        var allTerritories = mapManager.getAllTerritories();
        var allTerritoriesFromMap = mapManager.getTerritoriesAssignment().values().stream()
        													.flatMap(List::stream)
        													.collect(Collectors.toList());
        
        assertEquals(allTerritoriesFromMap.size(), allTerritories.size());
    }
    
    @Test
    void testGetOwner() {
    	
        var allTerritories = mapManager.getAllTerritories();
        assertFalse(allTerritories.isEmpty(), "Should have territories after initialization");

        var territory = allTerritories.get(0);
        var owner = mapManager.getOwner(territory);
        var ownedTerritories = mapManager.getTerritoriesOwnedBy(owner);
        
        assertTrue(ownedTerritories.contains(territory), 
                   "Territory should be in the owner's territory list");
    }
    
    @Test
    void testGetOwnerForAllTerritories() {
        var allTerritories = mapManager.getAllTerritories();

        for (String territory : allTerritories) {
            var owner = mapManager.getOwner(territory);
            var ownedByPlayer = mapManager.getTerritoriesOwnedBy(owner);
            
            assertTrue(ownedByPlayer.contains(territory), 
                       "Territory should be in owner's list");
        }
    }
    
    @Test
    void testCheckZoneCompletion() {
        var player = players.get(0);

        // Ottieni tutti i continenti disponibili
        var allParentZones = super.getParentZones();
        if (!allParentZones.isEmpty()) {
            var continent = allParentZones.get(0);
            var continentTerritories = super.getChildZones(continent);

            // Assegna manualmente tutti i territori di un continente al giocatore
            for (String territory : continentTerritories) {
                    mapManager.updateOwnership(player, territory);
            }

            var completedContinents = mapManager.checkZoneCompletion(player);

            assertNotNull(completedContinents);
            assertFalse(completedContinents.isEmpty());
            assertTrue(completedContinents.contains(continent));
        }
    }
}
