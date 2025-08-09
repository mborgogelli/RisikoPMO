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
	
	/**
	 * Trova una zona specifica per nome.
	 * 
	 * @param zoneName il nome della zona da cercare
	 * @return l'istanza di IZone corrispondente al nome, o null se non trovata
	 */
	public IZone findZoneByName(String zoneName);
	
	/**
	 * Trova la zona che contiene quella passata come parametro.
	 * 
	 * @param zoneName il nome della zona di cui vogliamo sapere il contenitore
	 * @return l'istanza di IZone corrispondente al conteniture, o null se non trovata
	 */
	public IZone whereIsZone(String zoneName);
	
	/**
	 * Restituisce le zone adiacenti a quella specificata.
	 * 
	 * @param zoneName il nome della zona di cui si vogliono conoscere i vicini
	 * @return lista di nomi delle zone adiacenti
	 */
	public List<String> getNeighbours(String zoneName);
	
	/**	
	 * Controlla se è possibile raggiungere una zona da un'altra.
	 * 
	 * @param zoneTo il nome della zona di destinazione
	 * @param zoneFrom il nome della zona di partenza
	 * @return true se la zona di destinazione è raggiungibile dalla zona di partenza, false altrimenti
	 */
	public boolean canReach(String zoneTo, String zoneFrom);
	
	/**
	 * Restituisce il valore di una zona specifica.
	 * 
	 * @param zoneName il nome della zona di cui si vuole conoscere il valore
	 * @return il valore della zona, o null se non trovata
	 */
	public Integer getValue(String zoneName);
	
}
