package model.versions.risikockassic.card;

import model.players.Player;

public class ConquerTerritoryMissionCard extends MissionCard {
	
	private final int territoriesToConquer;
	
	public ConquerTerritoryMissionCard(int territoriesToConquer) {
		super(EnumMissionCard.CONQUER, "Conquer " + territoriesToConquer + " territories");
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
