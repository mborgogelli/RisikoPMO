import it.uniurb.pmo.framework.management.interfaces.IManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * Classe base di setup per i test che utilizzano RisikoNew.
 * Inizializza la factory, i manager e i giocatori prima di ogni test.
 */
public abstract class RisikoNewTestSetup {

    protected GameFactoryRisikoNew gameFactory;
    protected List<IPlayer> players;
    protected IMapManagerRisikoNew mapManager;
    protected ITankManager tankManager;
    protected IMediatorRisikoNew mediator;

    @BeforeEach
    public void setUpRisikoNew() {
        gameFactory = new GameFactoryRisikoNew();

        players = createPlayers();

        mapManager = resolveManager(IMapManagerRisikoNew.class);
        tankManager = resolveManager(ITankManager.class);
        mediator = (IMediatorRisikoNew) gameFactory.getMediator();

        mapManager.initializeGame(players);
        tankManager.initializeGame(players);
    }

    protected List<IPlayer> createPlayers() {
        return List.of(
            new Player("Player1"),
            new Player("Player2"),
            new Player("Player3"),
            new Player("Player4")
        );
    }

    protected <T extends IManager> T resolveManager(Class<T> managerType) {
        return gameFactory.getManagers().stream()
            .filter(managerType::isInstance)
            .map(managerType::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Manager of type " + managerType.getName() + " not found"));
    }
}
