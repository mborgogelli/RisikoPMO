package model.management;

import java.util.List;

import model.players.IPlayer;

public interface IManager {
	
	/**
	 * Inizializza il gioco
	 */
	void initializeGame(List<IPlayer> players);
	
	Boolean isReady();

}
