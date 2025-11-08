package model.management;

import java.util.List;

import model.management.interfaces.IManager;
import model.management.interfaces.IMediator;
import model.players.IPlayer;

public abstract class PhaseManager implements IManager{
	
	private IMediator mediator;
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
}
