

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import it.uniurb.pmo.model.management.Director;
import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.GameVersion;

public class InitializationAndResetTest {
	
	private final List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
											new Player("Player2", EnumColors.YELLOW),
											new Player("Player3", EnumColors.BLUE));
	
	private final IDirector director = new Director(GameVersion.RISIKONEW, players);
	
}
