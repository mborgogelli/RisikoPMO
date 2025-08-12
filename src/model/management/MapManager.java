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
	
	private void requestGameMap(GameVersion gameversion) {
	    switch(gameversion) {
	        case RISIKOCLASSIC:
	            this.gameBoard = BoardCreatorRisikoClassic.getInstance().getMap();
	            break;
	        default:
	            throw new IllegalArgumentException("Unsupported game version: " + gameversion);
	    }
	}

	
	public IGameBoard getGameBoard() {
		if (this.gameBoard == null) {
			throw new IllegalStateException("Game board has not been initialized. Please request a game map first.");
		}
		return this.gameBoard;
	}
	
	public IGameBoard getGameBoard(GameVersion gameversion) {
		if (this.gameBoard != null && this.gameBoard.getGameVersion() == gameversion) {
			;
		}else {
			this.requestGameMap(gameversion);
		}
		return this.getGameBoard();
	}
}
