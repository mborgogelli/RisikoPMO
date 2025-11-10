package model.versions.risikockassic.card;

import model.board.IZone;
import model.card.ICard;
import model.card.ISymbolCard;
import model.versions.risikockassic.board.Territory;

public class TerritoryCard  implements ICard{
	
	private final EnumTerritoryTypeCard symbol;
    private final Territory territory;
    
    // Carta territorio con simbolo specifico
    TerritoryCard(EnumTerritoryTypeCard symbol, Territory territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
   // Carta jolly 
    TerritoryCard() {
		this.symbol = EnumTerritoryTypeCard.JOLLY;
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

}
