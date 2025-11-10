package model.versions.risikockassic.card;

import model.card.ISymbolCard;

public enum EnumTerritoryTypeCard implements ISymbolCard {
	
	INFANTRY,
	ARTILLERY,
	CAVALRY,
	JOLLY;
	
	@Override
	public ISymbolCard getSymbol() {
		return this;
	}

}
