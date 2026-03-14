package it.uniurb.pmo.model.management;

import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.management.interfaces.ITurnManager;

public abstract class TurnManager implements ITurnManager {
	
	private IMediator mediator;
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}


}
