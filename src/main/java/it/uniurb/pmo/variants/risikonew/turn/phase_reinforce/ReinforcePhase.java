package it.uniurb.pmo.variants.risikonew.turn.phase_reinforce;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.event.DeployRequestEvent;
import it.uniurb.pmo.framework.turn.event.PhaseResult;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewTerritorySymbols;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCard;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DeployCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReinforcePhase implements IPhase {

	private final ERisikoNewPhase phaseType;
	private boolean isCompleted;
	private final IMediatorRisikoNew mediator;
	private IPlayer player;
	private List<String> playerTerritories;

	public ReinforcePhase(IMediatorRisikoNew mediator) {
		this.mediator = mediator;
		this.isCompleted = false;
		this.phaseType = ERisikoNewPhase.REINFORCE;
	}

	@Override
	public ERisikoNewPhase getPhaseType() {
		return this.phaseType;
	}

	@Override
	public IGameEvent<IDeployRequestDTO> playPhase(IPlayer player) {
		this.clearPhase();
		this.player = player;
		this.playerTerritories = this.mediator.getZonesOwnedBy(player);
		this.reinforcePlayer();
		return new DeployRequestEvent(this.deployRequest());
	}

	@Override
	public void clearPhase() {
		this.player = null;
		this.playerTerritories = null;
		this.isCompleted = false;
	}

	@Override
	public IPhaseResult handleCommand(IGameCommand command) {
		if (this.isValidCommand(command)){
			DeployCommandRisikoNew deployCommand = (DeployCommandRisikoNew) command;
			this.isCompleted = true;
			this.deployTanks(deployCommand.deployment());
			return new PhaseResult(this.isCompleted, Optional.empty());
		} else {
			throw new IllegalArgumentException("Not valid command.");
		}
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		if(this.player == null) {
			throw new IllegalArgumentException("Player is null.");
		}

		boolean valid = command instanceof DeployCommandRisikoNew;
		if (valid) {
			DeployCommandRisikoNew deployCommand = (DeployCommandRisikoNew) command;

			valid = deployCommand.tokenType() == ERisikoNewToken.TANK
					&& deployCommand.deployment().values().stream().allMatch(tanks -> tanks > 0)
					&& deployCommand.deployment().keySet().stream()
						.allMatch(playerTerritories::contains)
					&& deployCommand.deployment().values().stream()
						.mapToInt(Integer::intValue)
						.sum() <= mediator.getPlayerTank(player);
		}
		return valid;
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
		Optional<List<ITerritoryCard>> tris = this.findBestCombination();
        tris.ifPresent(iTerritoryCards -> this.mediator.playTris(player, iTerritoryCards));
		return tris.map(this::getTrisScore).orElse(0);
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
				.map(ITerritoryCard::symbol)
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
                .filter(card -> mediator.getTerritoriesOwnedBy(player).contains(card.territoryName()))
                .count() * 2);
	}

	private void reinforcePlayer(){
		int reinforcementsFromTerritories = this.reinforceByTerritories();
		int reinforcementsFromContinents = this.reinforceByContinentBonus();
		int reinforcementsFromCards = this.reinforceByCards();
		int reinforcements = reinforcementsFromTerritories + reinforcementsFromContinents + reinforcementsFromCards;
		this.mediator.reinforcePlayer(player, reinforcements);
	}

	private IDeployRequestDTO deployRequest() {
		int tanks = this.mediator.getPlayerTank(this.player);
		if (tanks > 0) {
			return new DeployRequestRisikoNewDTO(this.player, this.playerTerritories, tanks);
		} else {
			throw new RuntimeException("Not enough tanks to deploy.");
		}
	}

	private void deployTanks(Map<String, Integer> targetZones) {
		targetZones.forEach((zone, tanks) -> this.mediator.deployTank(this.player, zone, tanks));
	}

}
