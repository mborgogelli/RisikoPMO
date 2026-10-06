package it.uniurb.pmo.variants.risikonew.turn.phase_combat;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.dto.IAttackChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IDefendRequestDTO;
import it.uniurb.pmo.framework.turn.event.AttackRequestEvent;
import it.uniurb.pmo.framework.turn.event.PhaseResult;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.AttackCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DefendCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.DefendRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CombatPhase implements IPhase {

	private final IMediatorRisikoNew mediator;
	private final ERisikoNewPhase phaseType;
	private boolean isCompleted;
	private IPlayer attacker;
	int attackerTanks;
	String attackerTerritory;
	private IPlayer defender;
	int defenderTanks;
	String defenderTerritory;
	private Map<String, List<String>> possibleTargets;

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
		this.acquirePlayerTargets(this.attacker);
		return new AttackRequestEvent(this.attackRequestDTO());
	}

	@Override
	public void clearPhase() {
		this.attacker = null;
		this.defender = null;
		this.attackerTanks = 0;
		this.defenderTanks = 0;
		this.attackerTerritory = null;
		this.defenderTerritory = null;
		this.possibleTargets = null;
		this.isCompleted = false;
	}

	@Override
	public IPhaseResult handleCommand(IGameCommand command) {
		boolean valid = this.isValidCommand(command);
		if (valid && command instanceof AttackCommandRisikoNew attackCommand){
			this.defender = this.mediator.getZoneOwner(attackCommand.defenderZone());
			this.attackerTerritory = attackCommand.attackerZone();
			this.attackerTanks = attackCommand.tanks();
			return new PhaseResult(false, Optional.of(this.defendRequestDTO()));
		}
		return null;
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

	private boolean checkDefendCommand(DefendCommandRisikoNew defendCommand) {
		return defendCommand.defenderTokens() > 0;
	}

	private void acquirePlayerTargets (IPlayer attacker){
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
				.filter(neighbour -> ownedZones.contains(neighbour))
				.filter(territory -> this.mediator.getZoneTank(territory) > 1)
				.toList();
	}

	private void attackPlayer(Optional<IAttackChoiceDTO> choice) {
	}

	private boolean checkAttackCommand(AttackCommandRisikoNew attackCommand) {
		boolean	valid = attackCommand.tokenType() == ERisikoNewToken.TANK
					&& this.possibleTargets.keySet().contains(attackCommand.attackerZone())
					&& this.possibleTargets.get(attackCommand.attackerZone()).contains(attackCommand.defenderZone())
					&& this.mediator.getZoneTank(attackCommand.attackerZone()) >= attackCommand.tanks()
					&& attackCommand.tanks() > 0
					&& attackCommand.tanks() <= 3;
		return valid;
	}

	private IAttackRequestDTO attackRequestDTO() {
		return new AttackRequestRisikoNewDTO(this.attacker, this.possibleTargets);
	}

	private IDefendRequestDTO defendRequestDTO() {
		int maxDefenderTanks = Math.max(this.mediator.getZoneTank(this.defenderTerritory),3);
		return new DefendRequestRisikoNewDTO(this.defender, maxDefenderTanks);
	}




}
