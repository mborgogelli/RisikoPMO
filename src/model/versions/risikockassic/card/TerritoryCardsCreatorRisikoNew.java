package model.versions.risikockassic.card;

import java.util.List;

import model.card.CardCreator;
import model.card.ICard;
import model.versions.risikockassic.board.*;

public class TerritoryCardsCreatorRisikoNew extends CardCreator{
	
	private static TerritoryCardsCreatorRisikoNew instance;
	
	
	protected TerritoryCardsCreatorRisikoNew() {
		super();
	}

	public static TerritoryCardsCreatorRisikoNew getInstance() {
		if (instance == null) {
			instance = new TerritoryCardsCreatorRisikoNew();
		}
		return instance;
	}

	@Override
	protected List<ICard> createCards() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void resetCards(List<ICard> cards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void configureCardDetails(ICard card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void configureVariantRules() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<ICard> loadCardsFromJson(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void saveCardsToJson(List<ICard> cards, String filePath) {
		// TODO Auto-generated method stub
		
	}
}
