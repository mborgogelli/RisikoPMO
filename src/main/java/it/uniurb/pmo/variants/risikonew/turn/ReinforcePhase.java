package it.uniurb.pmo.variants.risikonew.turn;

import java.util.List;
import java.util.Map;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
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
	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;
	private List<String> playerTerritories;
	private List<ICard> tris;

	public ReinforcePhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
		this.mediator = mediator;
		this.coordinator = coordinator;
	}

	@Override
	public int getStepId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player) {
		this.player = player;
		this.playerTerritories = this.mediator.getZonesOwnedBy(player);
		int reinforcements = this.reinforceByTerritories(this.playerTerritories);
		List<String> completedContinents = this.mediator.getCompletedContinents(player);
		for (String continent : completedContinents) {
			reinforcements = reinforcements + this.reinforceByContinentBonus(continent);
		}
		List<String> deployableZones = this.mediator.getZonesOwnedBy(player);
		Map<String, Integer> targetZones = this.coordinator.sendDeployRequest(player, deployableZones, reinforcements);
		targetZones.forEach((zone, tanks) -> this.mediator.deployTank(this.player, zone, tanks));
		this.clearPhase();
	}

	@Override
	public void nextStep(IPlayer player) {
	}

	@Override
	public void clearPhase() {
		this.player = null;
		this.playerTerritories = null;
		this.tris = null;
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
