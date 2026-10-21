package it.uniurb.pmo.variants.risikonew.turn.phase;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.phase.IPhase;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.phase.IPhasePayload;
import it.uniurb.pmo.framework.turn.phase.result.PhaseResult;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

import java.util.List;

public class PhaseStrategic implements IPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;

	public PhaseStrategic(IMediatorRisikoNew mediator) {
		this.mediator = mediator;
	}

	@Override
	public ERisikoNewPhase getPhaseType() {
		return ERisikoNewPhase.STRATEGIC;
	}

	@Override
	public IPhaseResult<? extends IPhasePayload> playPhase(IPlayer player) {
		this.player = player;
		List<String> ownedZones = this.mediator.getZonesOwnedBy(player);
		this.clearPhase();
		return new PhaseResult<>(false, java.util.Optional.empty());
	}

	@Override
	public void clearPhase() {
		this.player = null;
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
