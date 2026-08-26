package it.uniurb.pmo.variants.risikonew.card;

public record TerritoryCard(ERisikoNewTerritorySymbols symbol, String territoryName) implements ITerritoryCard {

	
	@Override
	public ERisikoNewCardType getCardType() {
		return ERisikoNewCardType.TERRITORY;
	}

}
