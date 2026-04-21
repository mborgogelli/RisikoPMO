import it.uniurb.pmo.model.management.interfaces.IGameFactory;
import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.ITurnManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.EnumPhase;
import it.uniurb.pmo.model.versions.risikockassic.GameFactoryRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.IMediatorRisikoNew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
        System.out.println(mediator.getAllTerritories());
        System.out.println(mediator.getAllZones());

		return gf.getManagers().stream()
				.filter(managerType::isInstance)
				.map(managerType::cast)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Manager of type " + managerType.getName() + " not found"));
	}

    @Test
    public void printPhases(){
        System.out.println(Arrays.stream(EnumPhase.values()).sorted().toList());
    }
}
