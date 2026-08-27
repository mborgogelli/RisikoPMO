package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewTerritorySymbols;
import it.uniurb.pmo.variants.risikonew.card.TerritoryCard;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_reinforce.ReinforcePhase;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ReinforcePhaseIntegrationTest extends RisikoNewTestSetup {

    private IGameCoordinatorRisikoNew gameCoordinator;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.gameCoordinator = super.getGameCoordinator();
    }

    @Test
    @DisplayName("Verifica che venga calcolato correttamente il numero di rinforzi da territori, continente e tris valido")
    void testReinforceWithContinentBonus() {
        IPlayer player1 = players.getFirst();
        IMediatorRisikoNew phaseMediator = spy(mediator);

        doReturn(List.of("topolinia", "paperopoli", "centinarola"))
                .when(phaseMediator).getZonesOwnedBy(player1);

        doReturn(List.of("oceania"))
                .when(phaseMediator).getCompletedContinents(player1);

        doReturn(2)
                .when(phaseMediator).getContinentArmyBonus("oceania");

        List<ICard> trisDebole = List.of(
                new TerritoryCard(ERisikoNewTerritorySymbols.INFANTRY, "topolinia"),
                new TerritoryCard(ERisikoNewTerritorySymbols.INFANTRY, "paperopoli"),
                new TerritoryCard(ERisikoNewTerritorySymbols.INFANTRY, "fano")
        );

        List<ICard> trisForte = List.of(
                new TerritoryCard(ERisikoNewTerritorySymbols.CAVALRY, "topolinia"),
                new TerritoryCard(ERisikoNewTerritorySymbols.CAVALRY, "paperopoli"),
                new TerritoryCard(ERisikoNewTerritorySymbols.CAVALRY, "pesaro")
        );

        List<ICard> trisInvalido = List.of(
                new TerritoryCard(ERisikoNewTerritorySymbols.CAVALRY, "topolinia"),
                new TerritoryCard(ERisikoNewTerritorySymbols.INFANTRY, "paperopoli"),
                new TerritoryCard(ERisikoNewTerritorySymbols.INFANTRY, "urbino")
        );

        doReturn(Stream.of(trisDebole,trisForte, trisInvalido))
                .when(phaseMediator).getAvailableTris(player1);

        int tanksBefore = mediator.getPlayerTank(player1);

        ReinforcePhase phase = new ReinforcePhase(phaseMediator, gameCoordinator);
        phase.playPhase(player1);
        int tanksAfter = mediator.getPlayerTank(player1);
        assertEquals(tanksBefore + 15, tanksAfter);
    }

}
