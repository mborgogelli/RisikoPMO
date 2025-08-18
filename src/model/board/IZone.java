package model.board;

import java.util.List;
import java.util.Optional;

import model.players.IPlayer;

/**
 * Interfaccia che rappresenta una generica zona della mappa di gioco, modellata come un contenitore che,
 * possibilmente, contiene o è contenuto a sua volta, in altre zone.
 * 
 */
public interface IZone {
	 
	/**
     * Restituisce il nome univoco della zona.
     * 
     * @return il nome della zona, non può essere null o vuoto
     */
	String getName();
	
	/**
	 * Restituisce il valore numerico associato alla zona.
	 * Ad esempio, il numero di armate iniziali per un territorio.
	 * 
	 * @return il valore della zona, non può essere null
	 */
	Integer getValue();
	
	 /**
	 * Imposta il valore numerico associato alla zona.
	 * 
	 * @param value il nuovo valore della zona, non può essere null
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
     * Restituisce una collezione non modificabile delle zone figlie.
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
     * Restituisce tutti i giocatori che possiedono parti di questa zona.
     * Per territori semplici, conterrà al massimo un giocatore.
     * Per continenti, può contenere più giocatori se parzialmente occupati.
     * 
     * @return Lista dei possessori
     */
    List<IPlayer> getOwners();
    
    /**
	 * Imposta il proprietario di questa zona.
	 * Utilizzato per assegnare il controllo iniziale o dopo un attacco.
	 * 
	 * @param players il giocatore che possiede la zona, può essere null se la zona è libera
	 */
    void setOwner(IPlayer player);
    
    
    /**
	 * Rimuove un giocatore dalla lista dei proprietari di questa zona.
	 * Utilizzato quando un giocatore perde il controllo della zona.
	 * 
	 * @param player il giocatore da rimuovere, se non presente non fa nulla
	 */
    void removeOwner(IPlayer player);
    
    /**
     * Verifica se un giocatore ha il controllo completo di questa zona.
     * 
     * @param giocatore l'ID del giocatore da verificare
     * @return true se il giocatore controlla completamente la zona
     */     
    Boolean isControlledBy(IPlayer p);
    
    /**
     * Restituisce le zone confinanti o collegate con questa zona
     * Utilizzato per determinare possibili attacchi o movimenti.
     * 
     * @return set delle zone confinanti
     */
    List<String> getNeighbours();
    
    /**
	 * Imposta le zone confinanti per questa zona.
	 * Utilizzato per costruire la mappa delle connessioni tra le zone.
	 * 
	 * @param neighbours Lista dei nomi delle zone confinanti
	 */
    void setNeighbours(List<String> neighbours);

   
}
