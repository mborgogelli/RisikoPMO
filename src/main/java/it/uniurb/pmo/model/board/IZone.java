package it.uniurb.pmo.model.board;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia che rappresenta una generica zona della mappa di gioco, modellata come un contenitore che,
 * possibilmente, contiene o è contenuto a sua volta, in altre zone.
 */
public interface IZone {

	/**
	 * Restituisce il nome della zona.
	 *
	 * @return il nome della zona, non può essere null
	 */
	String getName();
	
	/**
	 * Restituisce il valore numerico associato alla zona.
	 * Valore per la modalità "TIME ATTACK"
	 * 
	 * @return il valore della zona, non può essere null
	 */
	Integer getValue();

	/**
	 * Imposta il valore numerico associato alla zona.
	 *
	 * @param value Valore per la modalità "TIME ATTACK"
	 */
	void setValue(Integer value);
	
      /**
     * Restituisce la zona contenitore di questa zona.
     * Ad esempio, un territorio apparterrà a un continente.
     * 
     * @return la zona contenitore, null se questa è una zona di livello radice
     */
    Optional<IZone> getParentZone();
    
    /**
	 * Imposta la zona contenitore per questa zona.
	 * Utilizzato per costruire la gerarchia delle zone.
	 * 
	 * @param parent la zona contenitore, può essere null se questa è una zona di livello radice
	 */
    void setParentZone(IZone parent);
    
    /**
     * Restituisce una collezione (idealmente non modificabile) delle zone figlie.
     * Ad esempio, un continente conterrà i suoi territori.
     * 
     * @return Lista delle zone figlie
     */
    List<IZone> getChildZones();
    
    /**
	 * Imposta le zone figlie per questa zona.
	 * Utilizzato per costruire la gerarchia delle zone.
	 * 
	 */
    void setChildZones(List<IZone> zone);
    
    /**
     * Restituisce le zone confinanti o collegate con questa zona
     * Utilizzato per determinare possibili attacchi o movimenti.
     * 
     * @return set delle zone confinanti
     */
    List<String> getNeighbours();

    /**
	 * Imposta le zone confinanti o raggiungibili da questa zona.
	 * Utilizzato per costruire la mappa delle connessioni tra le zone.
	 * 
	 * @param neighbours Lista dei nomi delle zone confinanti
	 */
    void setNeighbours(List<String> neighbours);
    
 }
