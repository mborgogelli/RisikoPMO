package model.tabletop;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import model.IPlayer;

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
	public String getName();
	
    /**
     * Restituisce il tipo di zona (territorio, continente, ecc.).
     * 
     * @return il tipo di zona
     */
	public ZoneType getType();
        
      /**
     * Restituisce la zona contenitore di questa zona.
     * Ad esempio, un territorio apparterrà a un continente.
     * 
     * @return la zona contenitore, null se questa è una zona di livello radice
     */
    public Optional<IZone> getParentZone();
    
    /**
     * Restituisce una collezione non modificabile delle zone figlie.
     * Ad esempio, un continente conterrà i suoi territori.
     * 
     * @return Set delle zone figlie
     */
    Set<IZone> getChildZones();
    
    /**
     * Restituisce tutti i giocatori che possiedono parti di questa zona.
     * Per territori semplici, conterrà al massimo un giocatore.
     * Per continenti, può contenere più giocatori se parzialmente occupati.
     * 
     * @return Lista dei possessori
     */
    public List<IPlayer> getOwner();
    
    /**
     * Verifica se un giocatore ha il controllo completo di questa zona.
     * 
     * @param giocatore l'ID del giocatore da verificare
     * @return true se il giocatore controlla completamente la zona
     */     
    public Boolean isControlledBy(IPlayer p);
    
    /**
     * Restituisce le zone confinanti o collegate con questa zona
     * Utilizzato per determinare possibili attacchi o movimenti.
     * 
     * @return set delle zone confinanti
     */
    public Set<IZone> getBorders();
   
}
