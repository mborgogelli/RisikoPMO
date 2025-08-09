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

	private MapManager() {
	}

	public static MapManager getInstance() {
		if (instance == null) {
			instance = new MapManager();
		}
		return instance;
	}
	
	public void requestGameVersion(GameVersion gameversion) {
		switch(gameversion) {
		case GameVersion RisikoClassic:	IGameBoard gb = BoardCreatorRisikoClassic.getInstance().getMap();
		}
	}
}
