package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;

import java.util.Optional;

public interface ITurnManager extends IManager, IGameConductor {

	void playTurn(IPlayer p);

	void endTurn(IPlayer p);

	void playPhase(IPhase phase);

	int nextPhase();

	IPlayer getCurrentPlayer();

	IPlayer getNextPlayer();

	int getPlayedTurns();

	Optional<IPlayer> checkWinner();
	
}
