package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;

public interface IPhase extends IGameCommandReceiver {
	
	IGameEvent<? extends IGameState> playPhase(IPlayer player);

	void clearPhase();

}
