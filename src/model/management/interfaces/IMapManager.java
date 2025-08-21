package model.management.interfaces;


import java.util.List;
import java.util.Optional;

import model.board.IZone;
import model.players.IPlayer;

/**
 * Interfaccia per la gestione delle mappe di gioco.
 * Definisce i contratti per le operazioni territoriali indipendentemente dalla versione del gioco.
 */
public interface IMapManager extends IManager {
    
    /**
     * Verifica se è possibile spostare unità tra due territori.
     * @param fromTerritory il territorio di partenza
     * @param toTerritory il territorio di destinazione
     * @return true se lo spostamento è possibile
     */
    boolean canMoveBetween(String fromTerritory, String toTerritory);
    
    /**
     * Trova un territorio specifico per nome.
     * @param territoryName il nome del territorio
     * @return il territorio trovato
     */
    IZone findTerritoryByName(String territoryName);
    
    /**
     * Restituisce tutti i territori posseduti da un giocatore.
     * @param player il giocatore
     * @return lista dei territori posseduti
     */
    List<IZone> getTerritoriesOwnedBy(IPlayer player);
    
    /**
     * Aggiorna la proprietà di un territorio.
     * @param newOwner il nuovo proprietario
     * @param territory il territorio
     */
    void updateTerritoryOwnership(IPlayer newOwner, IZone territory);
    
    /**
     * Restituisce tutti i territori adiacenti a quello specificato.
     * @param territoryName il nome del territorio
     * @return lista dei nomi dei territori adiacenti
     */
    List<String> getAdjacentTerritories(String territoryName);
}