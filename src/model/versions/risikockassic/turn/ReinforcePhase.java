package model.versions.risikockassic.turn;

import java.util.List;

import model.card.ICard;
import model.players.IPlayer;
import model.versions.risikockassic.turn.interfaces.IReinforcePhase;

public class ReinforcePhase implements IReinforcePhase {
	
	private final static int BONUS_AFRICA = 3;
	private final static int BONUS_ASIA = 3;
	private final static int BONUS_AUSTRALIA = 3;
	private final static int BONUS_SOUTHAMERICA = 3;
	private final static int BONUS_EUROPE = 3;
	private final static int BONUS_NORTHAMERICA = 3;
	private final static int BONUS_DEFAULT = 0;
	
	private IPlayer player;
	private List<String> playerTerritories;
	private List<ICard> tris;
	private boolean started = false;
	
	@Override
	public void playPhase(IPlayer player) {
		if (!started) {
			this.player = player;
		}
	}

	@Override
	public int nextPhase() {
		int currentPhaseId = getPhaseId();
		int phaseCount = EnumPhaseRisikoNew.values().length;
		return (currentPhaseId + 1) % phaseCount;
	}

	@Override
	public void endPhase() {
		
	}

	@Override
	public int reinforceByTerritories(List<String> playerTerritories) {
		this.playerTerritories = playerTerritories;
		return this.playerTerritories.size();
	}

	@Override
	public int reinforceByContinentBonus(String continent) {
		int tanks;
		switch (continent) {
			case "africa" -> tanks = BONUS_AFRICA;
			case "asia" -> tanks = BONUS_ASIA;
			case "australia" -> tanks = BONUS_AUSTRALIA;
			case "south_america" -> tanks = BONUS_SOUTHAMERICA;
			case "europe" -> tanks = BONUS_EUROPE;
			case "north_america" ->	tanks = BONUS_NORTHAMERICA;
			default -> tanks = BONUS_DEFAULT;
		}
		return tanks;
	}

	@Override
	public int reinforceByCards(List<ICard> tris) {
		this.tris = tris;
		int bonus = getTerritoryCardBonusForOwnership(tris);
		return bonus;
	}
	
	/**
	 * Calcola il bonus ottenuto dal possesso dei territori indicati nelle carte
	 * 
	 * @param cards
	 * @return
	 */
	private int getTerritoryCardBonusForOwnership(List <ICard> cards) {
		int bonus = (int) cards.stream()
							.map(c -> c.getName())
							.filter(t -> this.playerTerritories.contains(t))
							.count();
		return bonus;
	}

}
