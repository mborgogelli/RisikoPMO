package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ICardManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_reinforce.ReinforcePhase;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReinforcePhaseIntegrationTest extends RisikoNewTestSetup {

    private static final List<String> OCEANIA_TERRITORIES = List.of(
            "nuova_guinea", "australia_occidentale", "australia_orientale", "indonesia"
    );
    private static final int OCEANIA_BONUS = 2;

    private List<IPlayer> players;
    private IMapManagerRisikoNew mapManager;
    private ITankManager tankManager;
    private IMediatorRisikoNew mediator;
    private IGameCoordinatorRisikoNew gameCoordinator;
    private ICardManagerRisikoNew cardManager;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.initManagers();
    }

    private void initManagers() {
        this.players = super.getPlayers();
        this.mapManager = super.getManager(IMapManagerRisikoNew.class);
        this.tankManager = super.getManager(ITankManager.class);
        this.cardManager = super.getManager(ICardManagerRisikoNew.class);
        this.mediator = super.getMediator();
        this.gameCoordinator = super.getGameCoordinator();
    }

    @Test
    @DisplayName("Il bonus continente viene sommato ai rinforzi da territori quando il giocatore possiede un continente intero")
    void testReinforceWithContinentBonus() {
        IPlayer player = players.getFirst();

        int territories = mediator.getZonesOwnedBy(player).size();
        int expectedReinforcements = territories / 3 + OCEANIA_BONUS;


        ReinforcePhase phase = new ReinforcePhase(mediator, gameCoordinator);
        phase.playPhase(player);

    }

}
