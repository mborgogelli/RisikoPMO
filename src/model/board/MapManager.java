package model.board;

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
	
	public void requestGameVersion(String Gameversion) {
		
	}
	
}
