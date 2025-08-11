package model.management;

import model.board.IGameBoard;
import model.utils.GameVersion;
import model.versions.risikockassic.board.BoardCreatorRisikoClassic;

public class MapManager {
	
	/**
	 * Gestisce la creazione e il caricamento della mappa di gioco.
	 * Utilizza il pattern Singleton per garantire un'unica istanza.
	 */
	private static MapManager instance;
	private IGameBoard gameBoard;
	
	
	private MapManager() {
	}

	public static MapManager getInstance() {
		if (instance == null) {
			instance = new MapManager();
		}
		return instance;
	}
	
	public void requestGameMap(GameVersion gameversion) {
		switch(gameversion) {
		case GameVersion RisikoClassic:	this.gameBoard = BoardCreatorRisikoClassic.getInstance().getMap();
		}
	}
	
	public IGameBoard getGameBoard() {
		if (this.gameBoard == null) {
			throw new IllegalStateException("Game board has not been initialized. Please request a game map first.");
		}
		return this.gameBoard;
	}
}
