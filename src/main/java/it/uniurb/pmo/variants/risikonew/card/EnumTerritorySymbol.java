package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ISymbolCard;

public enum EnumTerritorySymbol implements ISymbolCard {
	
	INFANTRY,
	ARTILLERY,
	CAVALRY,
	JOLLY;
	
	@Override
	public ISymbolCard getSymbol() {
		return this;
	}

}
