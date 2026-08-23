package it.uniurb.pmo.variants.risikonew.turn.phase_reinforce;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewTerritorySymbols;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCardContent;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployResponseRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

import java.util.ArrayList;
import java.util.List;

public class ReinforcePhase implements IPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;
	private List<String> playerTerritories;
	private List<ICard> tris;

	public ReinforcePhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
		this.mediator = mediator;
		this.coordinator = coordinator;
		this.tris = new ArrayList<>();
	}

	@Override
	public int getPhaseId() {
		return ERisikoNewPhase.REINFORCE.getId();
	}

	@Override
	public int getStepId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player) {
		this.player = player;
		this.playerTerritories = this.mediator.getZonesOwnedBy(player);
		int reinforcementsFromTerritories = this.reinforceByTerritories();
		int reinforcementsFromContinents = this.reinforceByContinentBonus();
		int reinforcementsFromCards = this.reinforceByCards();
		int reinforcements = reinforcementsFromTerritories + reinforcementsFromContinents + reinforcementsFromCards;
		List<String> deployableZones = this.mediator.getZonesOwnedBy(player);
		DeployResponseRisikoNewDTO response = (DeployResponseRisikoNewDTO) this.coordinator.sendDeployRequest(new DeployRequestRisikoNewDTO(player.getName(), player.getColor(), deployableZones, reinforcements));
		response.deployment().forEach((zone, tanks) -> this.mediator.deployTank(this.player, zone, tanks));
		this.clearPhase();
	}

	// TODO rimuovere nextStep
	@Override
	public void nextStep(IPlayer player) {
	}

	@Override
	public void clearPhase() {
		this.player = null;
		this.playerTerritories = null;
	}

	private int reinforceByTerritories() {
		return this.playerTerritories.size() / 3;
	}

	private int reinforceByContinentBonus() {
		int tanks = 0;
		List<String> completedContinents = this.mediator.getCompletedContinents(player);
		for (String c : completedContinents) {
			tanks += mediator.getContinentArmyBonus(c);
		}
		return tanks;
	}

	private int reinforceByCards() {
		List<ICard> cardsToPlay = this.mediator.getTerritoryCards(player);

		// Caso 1: Nessuna carta giocata o numero di carte insufficiente -> nessun bonus
		if (cardsToPlay == null || cardsToPlay.size() < 3) {
			return 0;
		}

		// Caso 2: almeno 3 carte disponibili
		int totalBonus = 0;
		totalBonus += evaluateTerritoryCardBonusForTriplet(cardsToPlay);

		return totalBonus;
	}

	private int evaluateTerritoryCardBonusForTriplet(List<ICard> cards) {
		return 0;
	}

	/**
	 * Calcola il bonus ottenuto dal possesso dei territori indicati nelle carte
	 * 
	 * @param cards le carte da valutare
	 * @return il bonus ottenuto
	 */
	private int getTerritoryCardBonusForOwnership(List <ICard> cards) {
		int bonus = (int) cards.stream()
							.map(c -> (ITerritoryCardContent) c.getCardContent())
							.filter(t -> this.playerTerritories.contains(t.getTerritoryName()))
							.count();
		return bonus * 2;
	}

	private List<ICard> checkForJockerAndTwoOfSameSymbol(List<ICard> cards) {
		return null;
	}

	private boolean checkForSymbolInCard(List<ICard> cards, ERisikoNewTerritorySymbols symbol) {
		return false;
	}

}
