package it.uniurb.pmo.variants.risikonew.turn.phase_combat;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.attackCommand;
import it.uniurb.pmo.framework.turn.dto.IAttackChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.event.AttackRequestEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

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
	private IPlayer defender;
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
		this.possibleTargets = null;
		this.isCompleted = false;
	}

	@Override
	public IPhaseResult handleCommand(IGameCommand command) {
		return null;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		if(this.attacker == null) {
			throw new IllegalArgumentException("Player is null.");
		}

		return command instanceof attackCommand;
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

	private IAttackRequestDTO attackRequestDTO() {
		return new AttackRequestRisikoNewDTO(this.attacker, this.possibleTargets);
	}


}
