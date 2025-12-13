package model.versions.risikockassic.turn;

import java.util.List;

import model.card.ICard;
import model.players.IPlayer;
import model.versions.risikockassic.turn.interfaces.IReinforcePhase;

public class ReinforcePhase implements IReinforcePhase {
	
	
	@Override
	public void playPhase() {
		// TODO Auto-generated method stub
	}

	@Override
	public int nextPhase() {
		int currentPhaseId = getPhaseId();
		int phaseCount = EnumPhaseRisikoNew.values().length;
		return (currentPhaseId + 1) % phaseCount;
	}

	@Override
	public void endPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public int reinforceByTerritories(List<String> playerTerritories) {
		return playerTerritories.size();
	}

	@Override
	public int reinforceByContinentBonus(String continent) {
		int tanks;
		switch (continent) {
			case "africa" -> tanks = 3;
			case "asia" -> tanks = 7;
			case "australia" -> tanks = 2;
			case "south_america" -> tanks = 2;
			case "europe" -> tanks = 5;
			case "north_america" ->	tanks = 5;
			default -> tanks = 0;
		}
		return tanks;
	}

	@Override
	public int reinforceByCards(IPlayer player, List<ICard> tris) {
		return 0;
	}
	
	private int getTerritoryCardBonusForOwnership() {
		return 0;
	}
	
}
