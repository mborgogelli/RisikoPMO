package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewTerritorySymbols;
import it.uniurb.pmo.variants.risikonew.card.TerritoryCard;
import it.uniurb.pmo.variants.risikonew.management.CardManagerRisikoNew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CardManagerTest {

    private AbstractCardManager cardManager;
    private List<ICard> cards;


    @BeforeEach
    public void setUp() {
        cardManager = new CardManagerRisikoNew();
        this.cards = new ArrayList<>();
    }

    @Test
    void testCombinationOf3With4Cards() {
        for (int i = 0; i < 5; i++) {
            this.cards.add(new TerritoryCard(ERisikoNewTerritorySymbols.INFANTRY,"Italia" + (i + 1)));
        }
        System.out.println(this.cards.size());
        System.out.println(cardManager.getCombinationsOf(this.cards, 3).toList().size());
    }



}
