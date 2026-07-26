package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.turn.interfaces.ICombatPhase;

public class CombatPhase implements ICombatPhase {

	private IPlayer player;
	private IMediator mediator;

	public CombatPhase() {
	}

	@Override
	public int getPhaseId() {
		return 0;
	}

	@Override
	public int getStepId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player, IMediator mediator) {
		this.player = player;
		this.mediator = mediator;
	}

	@Override
	public void nextStep(IPlayer player) {
	}

	@Override
	public void clearPhase() {

	}
}
