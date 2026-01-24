package it.uniurb.pmo.model.management;

import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.IMediator;

public abstract class TurnManager implements IManager{
	
	private IMediator mediator;
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
}
