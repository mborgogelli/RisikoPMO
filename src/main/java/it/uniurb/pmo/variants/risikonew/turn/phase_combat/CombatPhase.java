package it.uniurb.pmo.variants.risikonew.turn.phase_combat;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.PassCommand;
import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IDefendRequestDTO;
import it.uniurb.pmo.framework.turn.event.AttackRequestEvent;
import it.uniurb.pmo.framework.turn.event.PhaseResult;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;
import it.uniurb.pmo.framework.utils.DiceRoller;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.AttackCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DefendCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.CombatResultRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.DefendRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.*;
import java.util.stream.Stream;

public class CombatPhase implements IPhase {

	private final IMediatorRisikoNew mediator;
	private final ERisikoNewPhase phaseType;
	private boolean isCompleted;

	private IPlayer attacker;
	private String attackerTerritory;
	private int attackerTanks;
	private int attackerLosses;
	private Map<String, List<String>> possibleTargets;

	private IPlayer defender;
	private String defenderTerritory;
	private int defenderTanks;
	private int defenderLosses;

	public CombatPhase(IMediatorRisikoNew mediator) {
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
	public IGameEvent<IAttackRequestDTO> playPhase(IPlayer player) {
		this.attacker = player;
		this.acquirePlayerTargets();
		if(this.possibleTargets.isEmpty()){
			this.isCompleted = true;
			return new AttackRequestEvent(this.attackRequestDTO());
		}
		return new AttackRequestEvent(this.attackRequestDTO());
	}

	@Override
	public void clearPhase() {
		this.attacker = null;
		this.attackerTerritory = null;
		this.attackerTanks = 0;
		this.attackerLosses = 0;
		this.possibleTargets = null;

		this.defender = null;
		this.defenderTerritory = null;
		this.defenderTanks = 0;
		this.defenderLosses = 0;

		this.isCompleted = false;
	}

	@Override
	public IPhaseResult handleCommand(IGameCommand command) {
		if (command instanceof PassCommand) {
			this.isCompleted = true;
			return new PhaseResult(true, Optional.empty());
		} else {
			boolean valid = this.isValidCommand(command);
			if (valid && command instanceof AttackCommandRisikoNew(String attackerZone, String defenderZone, int tanks)) {
				this.defender = this.mediator.getZoneOwner(defenderZone);
				this.attackerTerritory = attackerZone;
				this.attackerTanks = tanks;
				this.defenderTerritory = defenderZone;
				return new PhaseResult(false, Optional.of(this.defendRequestDTO()));
			}
			if (valid && command instanceof DefendCommandRisikoNew(int tanks)) {
				this.defenderTanks = tanks;
				return new PhaseResult(true, Optional.of(this.combatResultDTO()));
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
		if (command instanceof AttackCommandRisikoNew attackCommand) {
			valid = this.checkAttackCommand(attackCommand);
		}
		if (command instanceof DefendCommandRisikoNew defendCommand) {
			valid = this.checkDefendCommand(defendCommand);
		}

		return valid;
	}

	private void acquirePlayerTargets (){
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

	private List<String> getStartingZones(String target, List<String> ownedZones) {
		return this.mediator.getNeighboursOf(target).stream()
				.filter(ownedZones::contains)
				.filter(territory -> this.mediator.getZoneTank(territory) > 1)
				.toList();
	}

	private boolean checkAttackCommand(AttackCommandRisikoNew attackCommand) {
		boolean	valid = attackCommand.tokenType() == ERisikoNewToken.TANK
					&& this.possibleTargets.containsKey(attackCommand.attackerZone())
					&& this.possibleTargets.get(attackCommand.attackerZone()).contains(attackCommand.defenderZone())
					&& this.mediator.getZoneTank(attackCommand.attackerZone()) >= attackCommand.tanks()
					&& attackCommand.tanks() > 0
					&& attackCommand.tanks() <= 3;
		return valid;
	}

	private boolean checkDefendCommand(DefendCommandRisikoNew defendCommand) {
		return defendCommand.defenderTokens() > 0
				&& defendCommand.defenderTokens() <= 3
				&& defendCommand.defenderTokens() <= this.mediator.getZoneTank(this.defenderTerritory);
	}

	private IAttackRequestDTO attackRequestDTO() {
		return new AttackRequestRisikoNewDTO(this.attacker, this.possibleTargets);
	}

	private IDefendRequestDTO defendRequestDTO() {
		int maxDefenderTanks = Math.max(this.mediator.getZoneTank(this.defenderTerritory),3);
		return new DefendRequestRisikoNewDTO(this.defender, maxDefenderTanks);
	}

	private CombatResultRisikoNewDTO combatResultDTO() {

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
			this.mediator.occupyTerritoryWithTanks(this.attacker, this.attackerTerritory, this.defenderTerritory, attackerRemainingTanks);
		}
		return new CombatResultRisikoNewDTO(this.attackerTerritory,this.defenderTerritory,
											attackRolls,defenseRolls,
											attackerLosses,defenderLosses,
											defenderIsDefeated);

	}

}
