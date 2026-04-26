package it.uniurb.pmo.variants.risikonew.card;

import java.util.List;

import it.uniurb.pmo.framework.card.CardCreator;
import it.uniurb.pmo.framework.card.ICard;

public class TerritoryCardsCreatorRisikoNew extends CardCreator{
	
	private static TerritoryCardsCreatorRisikoNew instance;
	private List<ICard> deck;
	
	
	protected TerritoryCardsCreatorRisikoNew() {
		super();
	}
	
	// Singleton Pattern
	public static TerritoryCardsCreatorRisikoNew getInstance() {
		if (instance == null) {
			instance = new TerritoryCardsCreatorRisikoNew();
		}
		return instance;
	}

	@Override
	protected List<ICard> createDeck() {
		// TODO Auto-generated method stub
		if (deck == null) {
			// TODO -- Assegnazione 
		    //List<IZone> territories = MapManagerRisikoNew.getInstance().getAllTerritories();
		    //for (IZone territory : territories) {
		    //    deck.add(new TerritoryCard(territory)); // Assicurati che TerritoryCard implementi ICard
		    //}
		}
		return this.deck;
	}

}
