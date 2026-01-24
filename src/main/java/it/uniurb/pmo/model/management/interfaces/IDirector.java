package it.uniurb.pmo.model.management.interfaces;


import java.util.Map;

import it.uniurb.pmo.model.players.IPlayer;

public interface IDirector extends IManager {
	
	boolean isGameStarted();

	boolean checkWin(IPlayer player);

	boolean checkLoss(IPlayer player);

	void StartGame();

	void exitGame(IPlayer player);
	
	Map<IManager,Boolean> getManagerStatus();
	
}
