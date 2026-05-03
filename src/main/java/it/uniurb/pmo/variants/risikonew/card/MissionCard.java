package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.Player;

//
public abstract class MissionCard {
	
	private final ICardType symbol;
	private final String description;
	
    // Carta territorio con simbolo specifico
    MissionCard(ICardType symbol, String description ) {
		this.symbol = symbol;
		this.description = description;
	}
    
	public String getName() {
		return description;
	}
	
	public ICardType getSymbol() {
		return this.symbol;
	}
	
	public abstract boolean isAchievementReached(Player player);
	
}
