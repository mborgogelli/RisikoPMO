package it.uniurb.pmo.model.management.interfaces;


import it.uniurb.pmo.model.players.IPlayer;

public interface IDirector extends IManager {
	
	void declareWinner(IPlayer player);

	void StartGame();

	void exitGame(IPlayer player);
	
	void stopGame();
}
