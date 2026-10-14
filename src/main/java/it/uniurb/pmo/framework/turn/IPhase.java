package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;

public interface IPhase {

	IPhaseType getPhaseType();
	
	IPhaseResult<? extends IGameState> playPhase(IPlayer player);

	void clearPhase();

	IPhaseResult<? extends IGameState> handleCommand(IGameCommand command);

	boolean isValidCommand(IGameCommand command);
}
