package it.uniurb.pmo.framework.board;

import java.util.List;
import java.util.Optional;

import it.uniurb.pmo.framework.utils.GameVersion;

public interface IGameBoard {
	
	/**
	 * Restituisce la lista delle zone "radice" della mappa.
	 * 
	 * @return lista di IZone che rappresentano i le zone "radice" della mappa
	 */
    List<IZone> getRootZones();

	/**
	 * Restituisce la versione del gioco.
	 * 
	 * @return versione del gioco
	 */
	GameVersion getGameVersion();
	
	/**
	 * Trova una zona specifica per nome.
	 * 
	 * @param zoneName il nome della zona da cercare
	 * @return l'istanza di IZone corrispondente al nome
	 */
	IZone findZoneByName(String zoneName);
	
	/**
	 * Trova la zona che contiene quella passata come parametro.
	 * 
	 * @param zoneName il nome della zona di cui vogliamo sapere la zona radice
	 * @return l'istanza di IZone corrispondente alla zona radice
	 */
	Optional<IZone> whereIsZone(String zoneName);
	
	/**
	 * Restituisce le zone adiacenti a quella specificata.
	 * 
	 * @param zoneName il nome della zona di cui si vogliono conoscere i vicini
	 * @return lista di nomi delle zone adiacenti
	 */
	List<String> getNeighbours(String zoneName);
	
	/**	
	 * Controlla se è possibile raggiungere una zona da un'altra.
	 * 
	 * @param zoneTo il nome della zona di destinazione
	 * @param zoneFrom il nome della zona di partenza
	 * @return true se la zona di destinazione è raggiungibile dalla zona di partenza, false altrimenti
	 */
	boolean canReach(String zoneTo, String zoneFrom);
	
	/**
	 * Restituisce il valore di una zona specifica.
	 * 
	 * @param zoneName il nome della zona di cui si vuole conoscere il valore
	 * @return il valore della zona
	 */
	Integer getValue(String zoneName);

	
}
