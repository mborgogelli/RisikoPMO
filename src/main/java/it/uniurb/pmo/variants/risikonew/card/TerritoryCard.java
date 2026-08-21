package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardContent;

// Carta territorio che rappresenta una carta del mazzo di Risiko Classic
public class TerritoryCard  implements ICard  {
	
	private final EnumSymbolRisikoNew symbol;
    private final ITerritoryCardContent cardContent;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(EnumSymbolRisikoNew symbol, ITerritoryCardContent cardContent ) {
		this.symbol = symbol;
		this.cardContent = cardContent;
	}
    

	@Override
	public EnumSymbolRisikoNew getCardType() {
		return this.symbol;
	}
	
	@Override
	public ICardContent getCardContent() {
		return this.cardContent;
	}

}
