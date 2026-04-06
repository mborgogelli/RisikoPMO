package it.uniurb.pmo.model.management.interfaces;

import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;

import java.util.Optional;

public interface ITurnManager extends IManager{

	void playTurn(IPlayer p);

	void endTurn(IPlayer p);

	void playPhase(IPhase phase);

	int nextPhase();

	IPlayer getCurrentPlayer();

	IPlayer getNextPlayer();

	int getCount();

	Optional<IPlayer> checkVictory();
	
	void startTurn();
}
