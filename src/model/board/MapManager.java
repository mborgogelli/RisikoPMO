package model.board;

public class MapManager {
	
	/**
	 * Gestisce la creazione e il caricamento della mappa di gioco.
	 * Utilizza il pattern Singleton per garantire un'unica istanza.
	 */
	private static MapManager instance;

	private IBoardCreator boardCreator;

	private MapManager(IBoardCreator boardCreator) {
		this.boardCreator = boardCreator;
	}

	public static MapManager getInstance(IBoardCreator boardCreator) {
		if (instance == null) {
			instance = new MapManager(boardCreator);
		}
		return instance;
	}

	public IBoardCreator getBoardCreator() {
		return this.boardCreator;
	}

}
