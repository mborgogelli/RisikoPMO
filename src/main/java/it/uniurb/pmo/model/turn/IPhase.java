package it.uniurb.pmo.model.turn;

import it.uniurb.pmo.model.players.IPlayer;

public interface IPhase {
	
	int getPhaseId();
	
	void playPhase(IPlayer player);
	
	int nextPhase();
	
	void endPhase();
}
