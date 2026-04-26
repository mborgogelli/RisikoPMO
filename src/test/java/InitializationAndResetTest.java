

import java.util.List;

import it.uniurb.pmo.framework.management.Director;
import it.uniurb.pmo.framework.management.interfaces.IDirector;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.framework.utils.GameVersion;

public class InitializationAndResetTest {
	
	private final List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
											new Player("Player2", EnumColors.YELLOW),
											new Player("Player3", EnumColors.BLUE));
	
	private final IDirector director = new Director(GameVersion.RISIKONEW, players);
	
}
