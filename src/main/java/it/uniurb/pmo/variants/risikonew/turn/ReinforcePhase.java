package it.uniurb.pmo.variants.risikonew.turn;

import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.turn.interfaces.IReinforcePhase;

public class ReinforcePhase implements IReinforcePhase {

	private final static int BONUS_AFRICA = 3;
	private final static int BONUS_ASIA = 3;
	private final static int BONUS_AUSTRALIA = 3;
	private final static int BONUS_SOUTHAMERICA = 3;
	private final static int BONUS_EUROPE = 3;
	private final static int BONUS_NORTHAMERICA = 3;
	private final static int BONUS_DEFAULT = 0;

	private IPlayer player;
	private IMediator mediator;
	private List<String> playerTerritories;
	private List<ICard> tris;
	private boolean isStarted = false;
	private boolean cardsUsed = false;

	@Override
	public int getStepId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player) {
		this.player = player;
		this.mediator = mediator;
		this.isStarted = true;
	}

	@Override
	public void nextStep(IPlayer player) {
	}

	@Override
	public void clearPhase() {

	}

	@Override
	public int reinforceByTerritories(List<String> playerTerritories) {
		this.playerTerritories = playerTerritories;
		return this.playerTerritories.size() / 3;
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
		this.tris.clear();
		return bonus * 2;
	}

	/**
	 * Calcola il bonus ottenuto dal possesso dei territori indicati nelle carte
	 * 
	 * @param cards
	 * @return
	 */
	private int getTerritoryCardBonusForOwnership(List <ICard> cards) {
		int bonus = (int) cards.stream()
							.map(c -> c.getCardContent())
							.filter(t -> this.playerTerritories.contains(t))
							.count();
		return bonus;
	}

}
