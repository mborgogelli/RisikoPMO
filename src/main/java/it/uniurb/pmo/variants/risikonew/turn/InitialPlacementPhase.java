package it.uniurb.pmo.variants.risikonew.turn;

import java.util.Optional;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;

public class InitialPlacementPhase implements IPhase {

	private IPlayer player;

	public InitialPlacementPhase() {
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player, IMediator mediator) {
		this.player = player;

	}

	@Override
	public void clearPhase() {

	}
}
