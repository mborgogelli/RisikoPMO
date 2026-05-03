package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;

// Carta territorio che rappresenta una carta del mazzo di Risiko Classic
public class TerritoryCard  implements ICard <EnumSymbolRisikoNew, String> {
	
	private final EnumSymbolRisikoNew symbol;
    private final String territory;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(EnumSymbolRisikoNew symbol, String territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
   // Carta jolly 
    TerritoryCard() {
		this.symbol = EnumSymbolRisikoNew.JOLLY;
		this.territory = null;
	}

	@Override
	public EnumSymbolRisikoNew getCardType() {
		return this.symbol;
	}
	
	@Override
	public String getCardContent() {
		return this.territory;
	}

}
