package model.management.interfaces;


import java.util.List;

import model.board.IZone;
import model.management.CardManager;
import model.management.TokenManager;
import model.players.IPlayer;

//TODO pattern Mediator
public interface IRuleManager extends IManager {
	
	// Metodi per accedere ai dati del MapManager
    List<IZone> getAllZones();
    List<IZone> getZonesOwnedBy(IPlayer player);
    boolean canMoveBetween(IPlayer player, IZone fromZone, IZone toZone);

    // Riferimenti ai manager
    IMapManager getMapManager();
    TokenManager getTokenManager();
    CardManager getCardManager();
}
