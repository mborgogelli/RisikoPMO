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
	 * Restituisce la versione del gioco.
	 * 
	 * @return versione del gioco
	 */
	public GameVersion getGameVersion();
	
	public IZone findZoneByName(String zoneName);
	
	public List<IZone> getNeighbours(String zoneName);
	
}
