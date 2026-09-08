package it.uniurb.pmo.variants.risikonew.turn.phase_combat;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.dto.IAttackChoiceDTO;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CombatPhase implements IPhase {

	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;
	private IPlayer attacker;
	private IPlayer defender;
	private Map<String, List<String>> possibleTargets;

	public CombatPhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
		this.mediator = mediator;
		this.coordinator = coordinator;
		this.possibleTargets = new HashMap<>();
	}

	@Override
	public int getPhaseId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player) {
		this.attacker = player;
		this.acquirePlayerTargets(this.attacker);
		Optional<IAttackChoiceDTO> choice = this.coordinator.sendAttackRequest(this.setAttackRequestDTO());
		if (choice.isPresent()) {
			this.attackPlayer(choice);
		}
	}

	@Override
	public void clearPhase() {
		this.attacker = null;
		this.defender = null;
		this.possibleTargets = null;
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

	private AttackRequestRisikoNewDTO setAttackRequestDTO() {
		return new AttackRequestRisikoNewDTO(this.attacker.getName(), this.attacker.getColor(), this.possibleTargets);
	}


}
