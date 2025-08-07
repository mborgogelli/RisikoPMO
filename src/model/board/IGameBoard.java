package model.board;

import java.util.List;

import model.utils.GameVersion;

public interface IGameBoard {
	
	/**
	 * Restituisce la lista dei continenti della mappa.
	 * 
	 * @return lista di IZone che rappresentano i continenti
	 */
	public List<IZone> getZones();
	
	/**
	 * Restituisce il nome della mappa di gioco.
	 * 
	 * @return nome della mappa
	 */
	public String getMapName();
	
	/**
	 * Restituisce la versione del gioco.
	 * 
	 * @return versione del gioco
	 */
	public GameVersion getGameVersion();
}
