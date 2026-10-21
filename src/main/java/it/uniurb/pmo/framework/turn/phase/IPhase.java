package it.uniurb.pmo.framework.turn.phase;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;

public interface IPhase {

	IPhaseType getPhaseType();

	void clearPhase();

	boolean isValidCommand(IGameCommand command);

	IPhaseResult<? extends IPhasePayload> playPhase(IPlayer player);

	IPhaseResult<? extends IPhasePayload> handleCommand(IGameCommand command);
}
