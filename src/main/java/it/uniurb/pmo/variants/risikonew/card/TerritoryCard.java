package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardContent;

// Carta territorio che rappresenta una carta del mazzo di Risiko Classic
public class TerritoryCard  implements ICard  {
	
	private final ERisikoNewTerritorySymbols symbol;
    private final String territory;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(ERisikoNewTerritorySymbols symbol, String territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
   // Carta jolly 
    TerritoryCard() {
		this.symbol = ERisikoNewTerritorySymbols.JOLLY;
		this.territory = null;
	}

	@Override
	public ERisikoNewTerritorySymbols getCardType() {
		return this.symbol;
	}
	
	@Override
	public ICardContent getCardContent() {
		return null;
	}

}
