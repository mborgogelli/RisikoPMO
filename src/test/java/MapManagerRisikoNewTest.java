
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.variants.risikonew.management.MapManagerRisikoNew;

class MapManagerRisikoNewTest extends MapManagerRisikoNew {

    private final MapManagerRisikoNew mapManager = new MapManagerRisikoNew();
    private final List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
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
        													.toList();
        
        assertEquals(allTerritoriesFromMap.size(), allTerritories.size());
    }
    
    @Test
    void testGetOwner() {
    	
        var allTerritories = mapManager.getAllTerritories();
        assertFalse(allTerritories.isEmpty(), "Should have territories after initialization");

        var territory = allTerritories.getFirst();
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
        var player = players.getFirst();

        // Ottieni tutti i continenti disponibili
        var allParentZones = super.getParentZones();
        if (!allParentZones.isEmpty()) {
            var continent = allParentZones.getFirst();
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
