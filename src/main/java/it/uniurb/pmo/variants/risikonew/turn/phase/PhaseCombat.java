package it.uniurb.pmo.variants.risikonew.turn.phase;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.PassCommand;
import it.uniurb.pmo.framework.turn.phase.IPhase;
import it.uniurb.pmo.framework.turn.phase.IPhasePayload;
import it.uniurb.pmo.framework.turn.phase.request.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IDefendRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.framework.turn.phase.result.PhaseResult;
import it.uniurb.pmo.framework.utils.DiceRoller;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.AttackCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DefendCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase.request.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase.request.DefendRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase.result.CombatResultRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PhaseCombat implements IPhase {

	private static final int MAX_DEPLOYABLE = 3;
	private final IMediatorRisikoNew mediator;
	private final ERisikoNewPhase phaseType;
	private boolean isCompleted;

	private IPlayer attacker;
	private String attackerTerritory;
	private int attackerTanks;
	private Map<String, Map<String,Integer>> possibleTargets;

	private IPlayer defender;
	private String defenderTerritory;
	private int defenderTanks;

	public PhaseCombat(IMediatorRisikoNew mediator) {
		this.mediator = mediator;
		this.possibleTargets = new HashMap<>();
		this.isCompleted = false;
		this.phaseType = ERisikoNewPhase.ATTACK;
	}

	@Override
	public ERisikoNewPhase getPhaseType() {
		return this.phaseType;
	}

	@Override
	public IPhaseResult<IGameRequest> playPhase(IPlayer player) {
		this.clearPhase();
		this.attacker = player;
		this.acquirePlayerTargets();
		this.isCompleted = this.possibleTargets.isEmpty();
		return new PhaseResult<>(this.isCompleted,
				this.isCompleted ? Optional.empty() : Optional.of(this.attackRequestDTO()));
	}

	@Override
	public void clearPhase() {
		this.attacker = null;
		this.attackerTerritory = null;
		this.attackerTanks = 0;
		this.possibleTargets = new HashMap<>();

		this.defender = null;
		this.defenderTerritory = null;
		this.defenderTanks = 0;

		this.isCompleted = false;
	}

	@Override
	public IPhaseResult<? extends IPhasePayload> handleCommand(IGameCommand command) {
		if (command instanceof PassCommand) {
			this.isCompleted = true;
			return new PhaseResult<>(this.isCompleted, Optional.empty());
		} else {
			boolean valid = this.isValidCommand(command);
			if (valid && command instanceof AttackCommandRisikoNew(String attackerZone, String defenderZone, int tanks)) {
				this.defender = this.mediator.getZoneOwner(defenderZone);
				this.attackerTerritory = attackerZone;
				this.attackerTanks = tanks;
				this.defenderTerritory = defenderZone;
				return new PhaseResult<>(this.isCompleted, Optional.of(this.defendRequestDTO()));
			}
			if (valid && command instanceof DefendCommandRisikoNew(int tanks)) {
				this.defenderTanks = tanks;
				CombatResultData combatData = this.combatResultData();
				this.acquirePlayerTargets();
				this.isCompleted = this.possibleTargets.isEmpty();
				CombatResultRisikoNewDTO combatResult = this.combatResultDTO(combatData);
				return new PhaseResult<>(this.isCompleted, Optional.of(combatResult));
			}
		}
		throw new IllegalArgumentException("Invalid command.");
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		if(this.attacker == null) {
			throw new IllegalArgumentException("Player is null.");
		}
		boolean valid = false;
		if (command instanceof PassCommand) {
			valid = true;
		}
		if (command instanceof AttackCommandRisikoNew attackCommand) {
			valid = this.checkAttackCommand(attackCommand);
		}
		if (command instanceof DefendCommandRisikoNew defendCommand) {
			valid = this.checkDefendCommand(defendCommand);
		}

		return valid;
	}

	private void acquirePlayerTargets (){
		this.possibleTargets.clear();
		List<String> ownedZones = this.mediator.getTerritoriesOwnedBy(this.attacker);
		this.getAllPossibleTargets(ownedZones)
				.forEach(target -> this.possibleTargets.put(target, this.getStartingZones(target, ownedZones)));
	}

	private Stream<String> getAllPossibleTargets(List<String> ownedZones) {
		return ownedZones.stream()
				.filter(territory -> this.mediator.getZoneTank(territory) > 1)
				.flatMap(territory -> this.mediator.getNeighboursOf(territory).stream())
				.distinct()
				.filter(territory -> !ownedZones.contains(territory));
	}

	private Map<String,Integer> getStartingZones(String target, List<String> ownedZones) {
		return this.mediator.getNeighboursOf(target).stream()
				.filter(ownedZones::contains)
				.filter(territory -> this.mediator.getZoneTank(territory) > 1)
				.collect(Collectors.toMap(startingZone -> startingZone,
						this::maxDeployableTanks));
	}

	private int maxDeployableTanks(String attackerZone) {
		return Math.min(this.mediator.getZoneTank(attackerZone) - 1, MAX_DEPLOYABLE);
	}

	private boolean checkAttackCommand(AttackCommandRisikoNew attackCommand) {
		boolean	valid = attackCommand.tokenType() == ERisikoNewToken.TANK
					&& this.possibleTargets.containsKey(attackCommand.defenderZone())
					&& this.possibleTargets.get(attackCommand.defenderZone()).containsKey(attackCommand.attackerZone())
					&& this.mediator.getZoneTank(attackCommand.attackerZone()) > attackCommand.tanks()
					&& attackCommand.tanks() > 0
					&& attackCommand.tanks() <= this.possibleTargets.get(attackCommand.defenderZone())
							.get(attackCommand.attackerZone());
		return valid;
	}

	private IAttackRequestDTO attackRequestDTO() {
		return new AttackRequestRisikoNewDTO(this.possibleTargets);
	}

	// Defend section

	private boolean checkDefendCommand(DefendCommandRisikoNew defendCommand) {
		return defendCommand.defenderTokens() > 0
				&& defendCommand.defenderTokens() <= 3
				&& defendCommand.defenderTokens() <= this.mediator.getZoneTank(this.defenderTerritory);
	}

	private IDefendRequestDTO defendRequestDTO() {
		int maxDefenderTanks = Math.min(this.mediator.getZoneTank(this.defenderTerritory), 3);
		return new DefendRequestRisikoNewDTO(this.defender, maxDefenderTanks);
	}

	private CombatResultRisikoNewDTO combatResultDTO(CombatResultData result) {
		Map<String, List<String>> possiblePlayerTargets = this.possibleTargets.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey,
						entry -> entry.getValue().keySet().stream().sorted().toList()));
		return new CombatResultRisikoNewDTO(this.attackerTerritory, this.defenderTerritory,
				result.attackRolls(), result.defenseRolls(), result.attackerLosses, result.defenderLosses,
				result.defenderIsDefeated(), possiblePlayerTargets);
	}

	private CombatResultData combatResultData() {

		// Generazione lanci
		List<Integer> attackRolls = new ArrayList<>();
		List<Integer> defenseRolls = new ArrayList<>();

		for (int i = 0; i < this.attackerTanks; i++) {
			attackRolls.add(DiceRoller.rollDice());
		}
		for (int i = 0; i < this.defenderTanks; i++) {
			defenseRolls.add(DiceRoller.rollDice());
		}

		// Ordinamento decrescente per confrontare i dadi in ordine di grandezza
		attackRolls.sort(Collections.reverseOrder());
		defenseRolls.sort(Collections.reverseOrder());

		int attackerLosses = 0;
		int defenderLosses = 0;

		// Verifica perdite: iterazione in base al numero minore di dadi lanciati (tanks dispiegati)
		int comparisons = Math.min(this.attackerTanks, this.defenderTanks);
		for (int i = 0; i < comparisons; i++) {
			if (attackRolls.get(i) > defenseRolls.get(i)) {
				defenderLosses++;
			} else {
				attackerLosses++;
			}
		}

		// Il mediatore aggiorna lo stato
		this.mediator.applyLosses(this.attackerTerritory, attackerLosses);
		this.mediator.applyLosses(this.defenderTerritory, defenderLosses);

		// In caso di vittoria dell'attaccante le armate restanti nell'ultimo scontro devono occupare il territorio conquistato
		boolean defenderIsDefeated = this.mediator.getZoneTank(this.defenderTerritory) == 0;
		if (defenderIsDefeated) {
			int attackerRemainingTanks = this.attackerTanks - attackerLosses;
			this.mediator.transferTanks(this.attacker, this.attackerTerritory, this.defenderTerritory, attackerRemainingTanks);
		}
		return new CombatResultData(attackRolls, defenseRolls, attackerLosses, defenderLosses, defenderIsDefeated);

	}

	private record CombatResultData(List<Integer> attackRolls, List<Integer> defenseRolls,
								int attackerLosses, int defenderLosses,
								boolean defenderIsDefeated) {
	}

}
