package it.uniurb.pmo.variants.risikonew.turn.phase;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.phase.IPhase;
import it.uniurb.pmo.framework.turn.phase.IPhasePayload;
import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.request.IMovementRequest;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.framework.turn.phase.result.PhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.MoveCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase.request.MovementRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PhaseStrategic implements IPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;
	private final Map<String, List<String>> destinations;
	private final Map<String, Integer> moveableTanksPerTerritory;
	private boolean isCompleted;


	public PhaseStrategic(IMediatorRisikoNew mediator) {
		this.mediator = mediator;
		this.destinations = new HashMap<>();
		this.moveableTanksPerTerritory = new HashMap<>();
		this.isCompleted = false;
	}

	@Override
	public ERisikoNewPhase getPhaseType() {
		return ERisikoNewPhase.STRATEGIC;
	}

	@Override
	public IPhaseResult<IGameRequest> playPhase(IPlayer player) {
		this.clearPhase();
		this.player = player;
		this.acquirePossibleDestinations();
		this.defineMoveableTanks();
		return new PhaseResult<>(this.isCompleted,
				this.isCompleted ? Optional.empty() : Optional.of(this.movementRequestRisikoNewDTO()));
	}

	@Override
	public void clearPhase() {
		this.player = null;
		this.destinations.clear();
		this.moveableTanksPerTerritory.clear();
		this.isCompleted = false;
	}

	@Override
	public IPhaseResult<? extends IPhasePayload> handleCommand(IGameCommand command) {
		if (isValidCommand(command)){
		}
		return null;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		if(this.player == null) {
			throw new IllegalArgumentException("Player is null.");
		}

		boolean valid = false;
		if (command instanceof MoveCommandRisikoNew(String source, String destination, int tanks)) {
			valid = this.destinations.containsKey(source) &&
					this.destinations.get(source).contains(destination) &&
					this.moveableTanksPerTerritory.get(source) >= tanks;
		}
		return valid;
	}

	private void defineMoveableTanks() {
		this.destinations.keySet()
				.forEach(territory -> this.moveableTanksPerTerritory.put(territory, this.mediator.getZoneTank(territory) - 1));
	}

	private void acquirePossibleDestinations() {
		List<String> ownedZones = this.mediator.getTerritoriesOwnedBy(this.player);
		ownedZones.stream()
				.filter(territory -> this.mediator.getZoneTank(territory) > 1)
				.forEach(territory -> this.destinations.put(territory, (this.getOwnedNeighbors(territory))));
		if (this.destinations.isEmpty()){
			this.isCompleted = true;
		}
	}

	private List<String> getOwnedNeighbors(String zone){
		List<String> ownedZones = this.mediator.getTerritoriesOwnedBy(this.player);
		return this.mediator.getNeighboursOf(zone).stream()
				.filter(ownedZones::contains)
				.toList();
	}

	private IMovementRequest movementRequestRisikoNewDTO() {
		return new MovementRequestRisikoNewDTO(this.destinations,this.moveableTanksPerTerritory);
	}
}
