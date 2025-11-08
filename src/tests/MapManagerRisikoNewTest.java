package tests;

import model.players.IPlayer;
import model.players.Player;
import model.utils.EnumColors;
import model.versions.risikockassic.managers.MapManagerRisikoNew;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MapManagerRisikoNewTest {

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
		var allZones = mapManager.getAllZones();
		assertNotNull(allZones);
		assertFalse(allZones.isEmpty(), "The map should contain zones after initialization.");

		var totalTerritories = allZones.size();
		var territoriesPerPlayer = totalTerritories / players.size();

		for (IPlayer player : players) {
			var ownedZones = mapManager.getZonesOwnedBy(player);
			assertNotNull(ownedZones, "Owned zones should not be null.");
			assertEquals(territoriesPerPlayer, ownedZones.size(),
					"Each player should own an equal number of territories.");
		}
	}


}
