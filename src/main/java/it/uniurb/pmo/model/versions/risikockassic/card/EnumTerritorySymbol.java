package it.uniurb.pmo.model.versions.risikockassic.card;

import it.uniurb.pmo.model.card.ISymbolCard;

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
