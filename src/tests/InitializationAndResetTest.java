package tests;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.management.Director;
import model.management.interfaces.IDirector;
import model.management.interfaces.IManager;
import model.players.IPlayer;
import model.players.Player;
import model.utils.EnumColors;
import model.utils.GameVersion;

public class InitializationAndResetTest {
	
	private List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
											new Player("Player2", EnumColors.YELLOW),
											new Player("Player3", EnumColors.BLUE));
	
	private IDirector director = new Director(GameVersion.RISIKONEW);
	
	@BeforeEach
	public void setUp(){
		this.director.initializeGame(players);
	}
	
	@Test
	public void testManagerStatus() {
		Map<IManager, Boolean> status = director.getManagerStatus();
		assertTrue(status.values().stream().allMatch(active -> active));
	}
	
}
