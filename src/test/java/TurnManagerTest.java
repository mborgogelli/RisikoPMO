import it.uniurb.pmo.model.management.interfaces.IGameFactory;
import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.management.interfaces.ITurnManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.versions.risikockassic.GameFactoryRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.management.MediatorRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.turn.TurnManagerRisikoNew;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class TurnManagerTest {

    private IGameFactory gf = new GameFactoryRisikoNew();
    private List<IPlayer> players = List.of(new Player("Player1", EnumColors.RED),
                                            new Player("Player2", EnumColors.YELLOW),
                                            new Player("Player3", EnumColors.BLUE));
    private ITurnManager turnManager;
    private IMediator mediator;

    @BeforeEach
    void setUp(){
        this.mediator = gf.getMediator();

        this.turnManager = ((MediatorRisikoNew) this.mediator).getTurnManager();
    }
}
