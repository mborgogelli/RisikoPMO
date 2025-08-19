package model.versions.risikockassic.card;

import model.board.IZone;
import model.card.ICard;
import model.card.ISymbolCard;
import model.versions.risikockassic.board.Territory;

public class TerritoryCard  implements ICard {
	
	private final EnumTerritoryCard symbol;
    private final Territory territory;
    
    TerritoryCard(EnumTerritoryCard symbol, Territory territory ) {
		this.symbol = symbol;
		this.territory = territory;
	}
    
    TerritoryCard() {
		this.symbol = EnumTerritoryCard.JOLLY;
		this.territory = null;
	}
    

	@Override
	public IZone getZone() {
		return this.territory;
	}

	@Override
	public ISymbolCard getSymbol() {
		return this.symbol;
	}

}
