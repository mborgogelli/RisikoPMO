package it.uniurb.pmo.model.management;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniurb.pmo.model.board.IBoardCreator;
import it.uniurb.pmo.model.board.IGameBoard;
import it.uniurb.pmo.model.board.IZone;
import it.uniurb.pmo.model.management.interfaces.IMapManager;
import it.uniurb.pmo.model.management.interfaces.IMediator;

/* Classe astratta per la gestione delle mappe di gioco.
 * Fornisce metodi per ottenere la mappa di gioco in base alla versione del gioco.
 * 
 */
public abstract class MapManager implements IMapManager {
	
	private final IGameBoard gameBoard;
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
	 * Restituisce tutte le zone di gioco.
	 * 
	 * @return lista di nomi di tutte le zone
	 */
	@Override
	public List<String> getAllZones() {
		return this.gameBoard.getZones().stream()
						.flatMap(root -> root.getChildZones().stream())
						.map(zone -> zone.getName())
						.collect(Collectors.toList());
	}

	@Override
	public Map<String,Integer> getZoneCountByRootZone(){
		return this.gameBoard.getZones().stream()
						.collect(Collectors.toMap(
								zone -> zone.getName(),
								zone -> zone.getChildZones().size()
						));
	}

	protected List<String> getParentZones(){
		return this.gameBoard.getZones().stream()
										.map(zone -> zone.getName())
										.toList();
	}
	
	protected List<String> getChildZones(String rootZone){
		List<IZone> childZones = this.gameBoard.findZoneByName(rootZone).getChildZones();
		return childZones.stream()
						.map(zone -> zone.getName())
						.toList();
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
