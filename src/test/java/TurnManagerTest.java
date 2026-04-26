import it.uniurb.pmo.framework.management.interfaces.IGameFactory;
import it.uniurb.pmo.framework.management.interfaces.IManager;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class TurnManagerTest {

    private IGameFactory gf = new GameFactoryRisikoNew();
    private List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
                                            new Player("Player2", EnumColors.YELLOW),
                                            new Player("Player3", EnumColors.BLUE));
    private ITurnManager turnManager;

    @BeforeEach
    void setUp(){
        this.turnManager = this.resolveManager(ITurnManager.class);
    }

	private <T extends IManager> T resolveManager(Class<T> managerType) {
        IMediatorRisikoNew mediator = (IMediatorRisikoNew) gf.getMediator();

		return gf.getManagers().stream()
				.filter(managerType::isInstance)
				.map(managerType::cast)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Manager of type " + managerType.getName() + " not found"));
	}

}
