package it.uniurb.pmo.variants.risikonew.turn.phase_reinforce;

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
	private Optional<List<ITerritoryCard>> tris;

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
		this.clearPhase();
		this.player = player;
		this.playerTerritories = this.mediator.getZonesOwnedBy(player);
		int reinforcementsFromTerritories = this.reinforceByTerritories();
		int reinforcementsFromContinents = this.reinforceByContinentBonus();
		int reinforcementsFromCards = this.reinforceByCards();
		int reinforcements = reinforcementsFromTerritories + reinforcementsFromContinents + reinforcementsFromCards;
		this.mediator.reinforcePlayer(this.player, reinforcements);
		DeployResponseRisikoNewDTO response = (DeployResponseRisikoNewDTO) this.coordinator.sendDeployRequest(new DeployRequestRisikoNewDTO(player.getName(), player.getColor(), this.playerTerritories, reinforcements));
		response.deployment().forEach((zone, tanks) -> this.mediator.deployTank(this.player, zone, tanks));
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

	private Optional<List<ITerritoryCard>> findBestCombination() {
		return this.mediator.getAvailableTris(this.player)
				.filter(this::isTrisValid)
				.max(Comparator.comparingInt(this::getTrisScore));
	}
	
	private boolean isTrisValid(List<ITerritoryCard> tris) {
		return this.getTrisScore(tris) > 0;
	}

	private int getTrisScore(List<ITerritoryCard> tris) {
		List<ERisikoNewTerritorySymbols> symbols = tris.stream()
				.map(card -> (card).symbol())
				.toList();
		int value = 0;

		// se contiene almeno un jolly, il punteggio è 12
		boolean trisWithJolly = symbols.contains(ERisikoNewTerritorySymbols.JOLLY) && (symbols.stream().distinct().count() == 2);

		if (trisWithJolly) {
			value = 12;
		}

		// se contiene tre simboli uguali
		boolean trisWithSameSymbols = symbols.get(0) == symbols.get(1) && symbols.get(1) == symbols.get(2);

		if (trisWithSameSymbols) {
			value = switch (symbols.getFirst()) {
				case INFANTRY -> 6 ;
				case CAVALRY -> 8;
				case ARTILLERY -> 10;
				default -> 0;
			};
		}

		// se contiene tre simboli diversi
		boolean trisWithDifferentSymbols = symbols.stream().distinct().count() == 3;
		if (trisWithDifferentSymbols) {
			value = 10;
		}

		if (value != 0){
			value += this.addBonusForTerritoryOwnership(tris);
		}

		// tris non valido
		return value;
	}

	private int addBonusForTerritoryOwnership(List<ITerritoryCard> tris) {
		return Math.toIntExact(tris.stream()
                .filter(card -> card.symbol() != ERisikoNewTerritorySymbols.JOLLY)
                .filter(card -> mediator.getTerritoriesOwnedBy(this.player).contains(card.territoryName()))
                .count() * 2);
	}
}
