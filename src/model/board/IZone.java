package model.board;

import java.util.List;
import java.util.Optional;
import model.IPlayer;
import model.utils.IEnumRisiko;

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
     * Restituisce il tipo di zona (territorio, continente, ecc.).
     * 
     * @return il tipo di zona
     */
	IEnumRisiko getType();
        
      /**
     * Restituisce la zona contenitore di questa zona.
     * Ad esempio, un territorio apparterrà a un continente.
     * 
     * @return la zona contenitore, null se questa è una zona di livello radice
     */
    Optional<IZone> getParentZone();
    
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
    List<IZone> getNeighbours();
   
}
