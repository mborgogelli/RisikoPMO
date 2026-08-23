package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardContent;
import it.uniurb.pmo.framework.card.ICardType;

// Carta territorio che rappresenta una carta del mazzo di Risiko New
public class TerritoryCard  implements ICard  {
	
	private final ICardContent content;
	private final ERisikoNewCardType cardType;

    TerritoryCard(ERisikoNewTerritorySymbols symbol, String territory) {
		this.cardType = ERisikoNewCardType.TERRITORY;
		this.content = new TerritoryCardContent(symbol, territory);
	}

	@Override
	public ICardType getCardType() {
		return this.cardType;
	}
	
	@Override
	public ICardContent getCardContent() {
		return this.content;
	}

}
