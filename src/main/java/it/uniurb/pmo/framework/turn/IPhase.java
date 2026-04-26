package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;

public interface IPhase {
	
	int getId();
	
	void playPhase(IPlayer player);

	void clearPhase();

}
