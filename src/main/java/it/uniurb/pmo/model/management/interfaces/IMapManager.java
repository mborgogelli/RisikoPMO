package it.uniurb.pmo.model.management.interfaces;


import java.util.List;
import java.util.Map;

import it.uniurb.pmo.model.players.IPlayer;

/**
 * Interfaccia per la gestione delle mappe di gioco.
 * Definisce i contratti per le operazioni territoriali indipendentemente dalla versione del gioco.
 */
public interface IMapManager extends IManager {
    
    /**
     * Verifica se è possibile per un giocatore spostare token tra due territori.
     * @param player il giocatore che tenta lo spostamento
     * @param toTerritory il territorio di destinazione
     * @param fromTerritory il territorio di partenza
     * @return true se lo spostamento è possibile
     */
    boolean canMoveBetween(IPlayer player, String toZone, String fromZone);
    
    /**
     * Restituisce tutti i territori posseduti da un giocatore.
     * @param player il giocatore
     * @return lista dei territori posseduti
     */
    List<String> getZonesOwnedBy(IPlayer player);
    
    /**
     * Aggiorna la proprietà di un territorio.
     * @param newOwner il nuovo proprietario
     * @param territory il territorio
     */
    void updateOwnership(IPlayer newOwner, String zone);
    
	/**
	 * Restituisce il proprietario di un territorio.
	 * 
	 * @param territory il territorio
	 * @return il proprietario del territorio
	 */
    IPlayer getOwner(String zone);
    
    /**
     * Restituisce tutte le zone di gioco.
     * @return
     */
    List<String> getAllZones();
    
	/**
	 * Restituisce la mappa delle assegnazioni dei territori ai giocatori.
	 * 
	 * @return mappa delle assegnazioni
	 */
    Map<IPlayer, List<String>> getTerritoriesAssignment();
    
	/**
	 * Verifica il completamento delle zone da parte di un giocatore.
	 * @param player
	 * @return
	 */
    List<String> checkZoneCompletion(IPlayer player);

	Map<String,Integer> getZoneCountByRootZone();
    
}