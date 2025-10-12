package model.management.interfaces;


import java.util.List;
import java.util.Optional;

import model.board.IGameBoard;
import model.board.IZone;
import model.players.IPlayer;
import model.players.Player;

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
    boolean canMoveBetween(IPlayer player, IZone toZone, IZone fromZone);
    
    /**
     * Restituisce tutti i territori posseduti da un giocatore.
     * @param player il giocatore
     * @return lista dei territori posseduti
     */
    List<IZone> getZonesOwnedBy(IPlayer player);
    
    /**
     * Aggiorna la proprietà di un territorio.
     * @param newOwner il nuovo proprietario
     * @param territory il territorio
     */
    void updateOwnership(IPlayer newOwner, IZone zone);
    
    /**
     * Restituisce tutti i territori adiacenti a quello specificato che appartengono al giocatore.
     * @param territoryName il nome del territorio di riferimento
     * @param player il giocatore
     * @return lista dei territori adiacenti posseduti
     */
    List<IZone> getNeighboursOwnedBy(IZone zone, IPlayer player);
    
    /**
     * Restituisce tutte le zone di gioco.
     * @return
     */
    List<IZone> getAllZones();
}