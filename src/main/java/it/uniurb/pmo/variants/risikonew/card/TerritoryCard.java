package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.variants.risikonew.board.Territory;

// Carta territorio che rappresenta una carta del mazzo di Risiko Classic
public class TerritoryCard  implements ICard{
	
	private final ICardType symbol;
    private final Territory territory;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(ICardType symbol, Territory territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
   // Carta jolly 
    TerritoryCard() {
		this.symbol = EnumTerritorySymbol.JOLLY;
		this.territory = null;
	}

	@Override
	public String getName() {
		return this.territory.getName();
	}

	@Override
	public ICardType getCardType() {
		return this.symbol;
	}
	
	public Territory getTerritory() {
		return territory;
	}

	// per Agevolare il debug
	@Override
	public String toString() {
	    return territory.getName() + " (" + symbol + ")";
	}

}
