package model.management;

import model.management.interfaces.IManager;
import model.management.interfaces.IMediator;

public abstract class PhaseManager implements IManager{
	
	private IMediator mediator;
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
}
