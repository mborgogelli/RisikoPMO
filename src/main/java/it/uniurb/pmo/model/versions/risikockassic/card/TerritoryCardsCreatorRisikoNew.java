package it.uniurb.pmo.model.versions.risikockassic.card;

import java.util.List;

import it.uniurb.pmo.model.card.CardCreator;
import it.uniurb.pmo.model.card.ICard;
import it.uniurb.pmo.model.card.ISymbolCard;

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
