package it.uniurb.pmo.variants.risikonew.turn.phase;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.phase.IPhase;
import it.uniurb.pmo.framework.turn.phase.IPhasePayload;
import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.framework.turn.phase.result.PhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PhaseStrategic implements IPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;
	private Map<String, List<String>> destinations;
	private Map<String, Integer> moveableTanksPerTerritory;
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

	private IGameRequest movementRequestRisikoNewDTO() {
		return null;
	}

	private void defineMoveableTanks() {
	}

	private void acquirePossibleDestinations() {
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
		return null;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		return false;
	}
}
