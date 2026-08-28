package it.uniurb.pmo.variants.risikonew.turn.phase_combat;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.dto.AttackChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.AttackRequestDTO;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;

import java.util.List;
import java.util.Map;

public class CombatPhase implements IPhase {

	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;
	private IPlayer attacker;
	private IPlayer defender;
	private Map<String, List<String>> possibleTargets;

	public CombatPhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
		this.mediator = mediator;
		this.coordinator = coordinator;
	}

	@Override
	public int getPhaseId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player) {
		this.attacker = player;
		List<String> ownedZones = this.mediator.getTerritoriesOwnedBy(this.attacker);
		AttackChoiceDTO choice = this.coordinator.sendAttackRequest(new AttackRequestDTO(this.attacker, ownedZones));
		this.clearPhase();
	}

    @Override
	public void clearPhase() {
		this.attacker = null;
		this.defender = null;
		this.possibleTargets = null;
	}

	private void possibleTargets(){
	};

	private List<String> possibleTargets(IPlayer attacker){
		List<String> ownedZones = this.mediator.getTerritoriesOwnedBy(this.attacker);
		return ownedZones.stream()
				.filter(territory -> this.mediator.getZoneTank(territory) > 1)
				.flatMap(territory -> this.mediator.getNeighboursOf(territory).stream())
				.distinct()
				.filter(territory -> !ownedZones.contains(territory))
				.toList();
	}
}
