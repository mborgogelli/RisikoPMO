package it.uniurb.pmo.model.management;

import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.management.interfaces.ITurnManager;
import it.uniurb.pmo.model.utils.EnumPhase;

import java.util.List;

public abstract class TurnManager implements ITurnManager {
	
	private IMediator mediator;

	@Override
	public void setMediator(IMediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}

	protected abstract List<EnumPhase> getOrderedPhase();

}
