package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.phase.IPhase;
import it.uniurb.pmo.framework.turn.snapshot.IPlayerSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ITurnManager extends IManager, IGameConductor {

	void startTurn(IPlayer p);

	void endTurn(IPlayer p);

	void startPhase(IPhase phase);

	int nextPhase();

	IPlayer getCurrentPlayer();

	IPlayer getNextPlayer();

	int getPlayedTurns();

	Optional<IPlayer> checkWinner();

	List<IPlayer> getPlayers();

	Map<String, IPlayerSnapshot> gameSnapshot();

}

