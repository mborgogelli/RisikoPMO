package it.uniurb.pmo.model.versions.risikockassic.card;

import it.uniurb.pmo.model.players.Player;

public class ConquerTerritoryMissionCard extends MissionCard {
	
	private final int territoriesToConquer;
	
	public ConquerTerritoryMissionCard(int territoriesToConquer) {
		super(EnumMissionSymbol.CONQUER, "Conquer " + territoriesToConquer + " territories");
		this.territoriesToConquer = territoriesToConquer;
	}
	
	public int getTerritoriesToConquer() {
		return territoriesToConquer;
	}

	@Override
	public boolean isAchievementReached(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
