package it.uniurb.pmo.model.versions.risikockassic.card;

import it.uniurb.pmo.model.card.ISymbolCard;
import it.uniurb.pmo.model.players.Player;

//
public abstract class MissionCard {
	
	private final ISymbolCard symbol;
	private final String description;
	
    // Carta territorio con simbolo specifico
    MissionCard(ISymbolCard symbol, String description ) {
		this.symbol = symbol;
		this.description = description;
	}
    
	public String getName() {
		return description;
	}
	
	public ISymbolCard getSymbol() {
		return this.symbol;
	}
	
	public abstract boolean isAchievementReached(Player player);
	
}
