package it.uniurb.pmo.variants.risikonew.turn.phase_strategic;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;

import java.util.List;

public class StrategicPhase implements IStrategicPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;

	public StrategicPhase(IMediatorRisikoNew mediator) {
		this.mediator = mediator;
	}

	@Override
	public IGameEvent<? extends IGameState> playPhase(IPlayer player) {
		this.player = player;
		List<String> ownedZones = this.mediator.getZonesOwnedBy(player);
		this.clearPhase();
		return null;
	}

	@Override
	public void clearPhase() {
		this.player = null;
	}

	@Override
	public IPhaseResult handleCommand(IGameCommand command) {
		return null;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		return false;
	}
}
