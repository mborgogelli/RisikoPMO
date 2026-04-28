package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.management.interfaces.IMediator;

public interface IPhase {
	
	int getId();
	
	void playPhase(IPlayer player, IMediator mediator);

	void clearPhase();

}
