package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.players.Player;

public class ConquerTerritoryMissionCard extends AbstractMissionCard {
	
	private final int territoriesToConquer;
	
	public ConquerTerritoryMissionCard(int territoriesToConquer) {
		super(EnumMissionType.CONQUER, "Conquer " + territoriesToConquer + " territories");
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
