package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;

public record TerritoryCard(ERisikoNewTerritorySymbols symbol, String territory) implements ICard {

	
	@Override
	public ERisikoNewCardType getCardType() {
		return ERisikoNewCardType.TERRITORY;
	}
	
}
