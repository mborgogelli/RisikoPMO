package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;

public interface IPhase {
	
	IGameEvent<? extends IGameState> playPhase(IPlayer player);

	void clearPhase();

	IPhaseResult handleCommand(IGameCommand command);

	boolean isValidCommand(IGameCommand command);
}
