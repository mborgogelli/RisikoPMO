package it.uniurb.pmo.variants.risikonew.management;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapManagerRisikoNewIntegrationTest extends MapManagerRisikoNew {

    private final MapManagerRisikoNew mapManager = new MapManagerRisikoNew();
    private final List<IPlayer> players = List.of(new Player("Player1"),
											new Player("Player2"),
											new Player("Player3"));

    @BeforeEach
    void initialize() {
        this.mapManager.initializeGame(players);
    }

    @Test
    @DisplayName("Distribuisce equamente i territori tra i giocatori")
	void testMapInitialization() {
		List<String> allZones = mapManager.getAllTerritories();
		int totalTerritories = allZones.size();
		int territoriesPerPlayer = totalTerritories / players.size();

		for (IPlayer player : players) {
			List<String> ownedZones = mapManager.getZonesOwnedBy(player);
			
			assertNotNull(ownedZones, "Owned zones should not be null.");
			assertEquals(territoriesPerPlayer, ownedZones.size(),
					"Each player should own an equal number of territories.");
		}
	}
    
    @Test
    @DisplayName("Restituisce tutti i territori assegnati sulla mappa")
    void testGetAllTerritories() {
        List<String> allTerritories = mapManager.getAllTerritories();
        List<String> allTerritoriesFromMap = mapManager.getTerritoriesAssignment().values().stream()
        													.flatMap(List::stream)
        													.toList();
        
        assertEquals(allTerritoriesFromMap.size(), allTerritories.size());
    }
    
    @Test
    @DisplayName("Restituisce il proprietario di un territorio assegnato")
    void testGetOwner() {
    	
        List<String> allTerritories = mapManager.getAllTerritories();
        assertFalse(allTerritories.isEmpty(), "Should have territories after initialization");

        String territory = allTerritories.getFirst();
        IPlayer owner = mapManager.getOwner(territory);
        List<String> ownedTerritories = mapManager.getTerritoriesOwnedBy(owner);
        
        assertTrue(ownedTerritories.contains(territory),
                   "Territory should be in the owner's territory list");
    }
    
    @Test
    @DisplayName("Associa ogni territorio al rispettivo proprietario")
    void testGetOwnerForAllTerritories() {
        List<String> allTerritories = mapManager.getAllTerritories();

        for (String territory : allTerritories) {
            IPlayer owner = mapManager.getOwner(territory);
            List<String> ownedByPlayer = mapManager.getTerritoriesOwnedBy(owner);
            
            assertTrue(ownedByPlayer.contains(territory),
                       "Territory should be in owner's list");
        }
    }
    
    @Test
    @DisplayName("Rileva il completamento di un continente da parte di un giocatore")
    void testCheckZoneCompletion() {
        IPlayer player = players.getFirst();

        // Ottieni tutti i continenti disponibili
        List<String> allParentZones = super.getParentZones();
        if (!allParentZones.isEmpty()) {
            String continent = allParentZones.getFirst();
            List<String> continentTerritories = super.getChildZones(continent);

            // Assegna manualmente tutti i territori di un continente al giocatore
            for (String territory : continentTerritories) {
                    mapManager.updateOwnership(player, territory);
            }

            List<String> completedContinents = mapManager.checkZoneCompletion(player);

            assertNotNull(completedContinents);
            assertFalse(completedContinents.isEmpty());
            assertTrue(completedContinents.contains(continent));
        }
    }
}
