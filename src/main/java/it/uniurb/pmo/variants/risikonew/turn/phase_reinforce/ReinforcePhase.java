package it.uniurb.pmo.variants.risikonew.turn.phase_reinforce;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewTerritorySymbols;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCard;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployResponseRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReinforcePhase implements IPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;
	private List<String> playerTerritories;
	private Optional<List<ICard>> tris;

	public ReinforcePhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
		this.mediator = mediator;
		this.coordinator = coordinator;
	}

	@Override
	public int getPhaseId() {
		return ERisikoNewPhase.REINFORCE.getId();
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
		this.tris = this.findBestCombination();
		if (this.tris.isPresent()){
			this.mediator.playTris(this.player, this.tris.get());
		}
		return this.tris.map(this::getTrisScore).orElse(0);
	}

	private Optional<List<ICard>> findBestCombination() {
		return this.mediator.getAvailableTris(this.player)
				.filter(this::isTrisValid)
				.max(Comparator.comparingInt(this::getTrisScore));
	}
	
	private boolean isTrisValid(List<ICard> tris) {
		return this.getTrisScore(tris) > 0;
	}

	private int getTrisScore(List<ICard> tris) {
		List<ERisikoNewTerritorySymbols> symbols = tris.stream()
				.map(card -> ((ITerritoryCard) card).getSymbol())
				.toList();
		// se contiene almeno un jolly, il punteggio è 12
		boolean trisWithJolly = symbols.contains(ERisikoNewTerritorySymbols.JOLLY) && (symbols.stream().distinct().count() == 2);

		if (trisWithJolly) {
			return 12;
		}

		// se contiene tre simboli uguali
		boolean trisWithSameSymbols = symbols.get(0) == symbols.get(1) && symbols.get(1) == symbols.get(2);
		if (trisWithSameSymbols) {
			switch (symbols.get(0)) {
				case INFANTRY: return 6;
				case CAVALRY: return 8;
				case ARTILLERY: return 10;
				default: return 0;
			}
		}

		// se contiene tre simboli diversi
		boolean trisWithDifferentSymbols = symbols.stream().distinct().count() == 3;
		if (trisWithDifferentSymbols) {
			return 10;
		}
		// tris non valid0
		return 0;
	}

}
