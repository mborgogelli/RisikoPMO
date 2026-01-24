

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.model.management.Director;
import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.GameVersion;

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
