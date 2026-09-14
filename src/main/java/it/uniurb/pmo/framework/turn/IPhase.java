package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;

public interface IPhase extends IGameCommandReceiver {
	
	int getPhaseId();

	void playPhase(IPlayer player);

	void clearPhase();

}
