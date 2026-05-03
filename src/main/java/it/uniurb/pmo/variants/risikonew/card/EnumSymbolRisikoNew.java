package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICardType;

public enum EnumSymbolRisikoNew implements ICardType {
	
	INFANTRY,
	ARTILLERY,
	CAVALRY,
	JOLLY;
	
	@Override
	public ICardType getCardType() {
		return this;
	}

}
