package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;

public interface IPhase extends IGameCommandReceiver {
	
	int getPhaseId();

	void playPhase(IPlayer player);

	void clearPhase();

}
