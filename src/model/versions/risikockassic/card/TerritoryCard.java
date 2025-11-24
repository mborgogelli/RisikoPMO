package model.versions.risikockassic.card;

import model.card.ICard;
import model.card.ISymbolCard;
import model.versions.risikockassic.board.Territory;
// Carta territorio che rappresenta una carta del mazzo di Risiko Classic
public class TerritoryCard  implements ICard{
	
	private final ISymbolCard symbol;
    private final Territory territory;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(ISymbolCard symbol, Territory territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
   // Carta jolly 
    TerritoryCard() {
		this.symbol = EnumTerritoryCard.JOLLY;
		this.territory = null;
	}

	@Override
	public String getName() {
		return this.territory.getName();
	}

	@Override
	public ISymbolCard getSymbol() {
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
