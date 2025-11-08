package tests;

import java.util.List;

import org.junit.jupiter.api.Test;

import model.management.Director;
import model.management.interfaces.IDirector;
import model.management.interfaces.IGameFactory;
import model.players.IPlayer;
import model.players.Player;
import model.utils.EnumColors;
import model.utils.GameVersion;
import model.versions.risikockassic.GameFactoryRisikoNew;

public class InitializationAndResetTest {
	
	private List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
											new Player("Player2", EnumColors.YELLOW),
											new Player("Player3", EnumColors.BLUE));
	
	private IDirector director = new Director(GameVersion.RISIKONEW);
	
	private IGameFactory gf = new GameFactoryRisikoNew();
	
	@Test
	public void setUp(){
		
		System.out.println(gf.getManagers().size());
		
	}
}
