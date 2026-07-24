package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.management.interfaces.IMediator;

public interface IPhase {
	
	int getId();

	// TODO puoi evitare di passare il mediator mettendolo come parametro nel costruttore
	void playPhase(IPlayer player, IMediator mediator);

	void clearPhase();

}
