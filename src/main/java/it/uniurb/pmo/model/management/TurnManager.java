package it.uniurb.pmo.model.management;

import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.management.interfaces.ITurnManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;
import it.uniurb.pmo.model.utils.EnumPhase;

import java.util.LinkedList;
import java.util.List;

public abstract class TurnManager implements ITurnManager {
	
	private IMediator mediator;

	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}

	protected abstract List<EnumPhase> getOrderedPhase();

}
