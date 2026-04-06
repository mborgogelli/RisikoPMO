package it.uniurb.pmo.model.turn;

import it.uniurb.pmo.model.players.IPlayer;

public interface IPhase {
	
	int getId();
	
	void playPhase(IPlayer player);

}
