package model.management;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.board.IBoardCreator;
import model.board.IGameBoard;
import model.board.IZone;
import model.management.interfaces.IManager;
import model.management.interfaces.IMapManager;
import model.management.interfaces.IMediator;
import model.players.IPlayer;
import model.utils.GameVersion;

/* Classe astratta per la gestione delle mappe di gioco.
 * Fornisce metodi per ottenere la mappa di gioco in base alla versione del gioco.
 * 
 */
public abstract class MapManager implements IMapManager {
	
	private IGameBoard gameBoard;
	private IMediator mediator;
	
	/**
	 * Costruttore che accetta un BoardCreator per inizializzare la mappa di gioco.
	 * 
	 * @param boardCreator il creatore della mappa di gioco
	 */
	public MapManager(IBoardCreator boardCreator) {
		this.gameBoard = boardCreator.getMap();
	}
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
	
	/**
	 * Utility method per le sottoclassi
	 * Restituisce tutti i territori della mappa.
	 * 
	 * @return lista dei territori
	 */
	@Override
	public List<String> getAllZones() {
		return this.gameBoard.getZones().stream()
						.flatMap(continent -> continent.getChildZones().stream())
						.map(zone -> zone.getName())
						.collect(Collectors.toList());
	}
	
	protected <T extends IZone> List<IZone> findZoneByType(Class<T> zoneType) {
		List<IZone> myZones = new ArrayList<>();
		
		if (myZones.isEmpty()) {
	     	throw new IllegalArgumentException("Manager of type " + zoneType + " not found.");
	    }
        return myZones;
    }
	
	protected IZone findZoneByName(String territoryName) {
        return this.gameBoard.findZoneByName(territoryName);
    }

    protected List<String> getNeighbours(String territoryName) {
        return this.gameBoard.getNeighbours(territoryName);
    }

    protected boolean canMoveBetween(String toTerritory, String fromTerritory) {
        return this.gameBoard.canReach(toTerritory, fromTerritory);
    }
    
    
    
}
