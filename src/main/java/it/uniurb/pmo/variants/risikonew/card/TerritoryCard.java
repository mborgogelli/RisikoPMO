package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.variants.risikonew.board.Territory;

// Carta territorio che rappresenta una carta del mazzo di Risiko Classic
public class TerritoryCard  implements ICard <EnumTerritorySymbol, String> {
	
	private final EnumTerritorySymbol symbol;
    private final String territory;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(EnumTerritorySymbol symbol, String territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
   // Carta jolly 
    TerritoryCard() {
		this.symbol = EnumTerritorySymbol.JOLLY;
		this.territory = null;
	}

	@Override
	public EnumTerritorySymbol getCardType() {
		return this.symbol;
	}
	
	@Override
	public String getCardContent() {
		return this.territory;
	}

}
