package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;

public interface IPhase {
	
	int getPhaseId();

	int getStepId();

	void playPhase(IPlayer player);

	void nextStep(IPlayer player);

	void clearPhase();

}
