package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.board.IBoardCreator;
import it.uniurb.pmo.framework.board.IGameBoard;
import it.uniurb.pmo.framework.board.IZone;
import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.management.interfaces.IMediator;

import java.util.List;
import java.util.stream.Collectors;

/* Classe astratta per la gestione delle mappe di gioco.
 * Fornisce metodi per ottenere la mappa di gioco in base alla versione del gioco.
 * 
 */
public abstract class AbstractMapManager implements IMapManager {
	
	private final IGameBoard gameBoard;

	/**
	 * Costruttore che accetta un BoardCreator per inizializzare la mappa di gioco.
	 * 
	 * @param boardCreator il creatore della mappa di gioco
	 */
	public AbstractMapManager(IBoardCreator boardCreator) {
		this.gameBoard = boardCreator.getMap();
	}
	
	@Override
	public void setMediator(IMediator mediator) {
		mediator.registerManager(this);
	}
	
	/**
	 * Restituisce tutte le zone di gioco.
	 * 
	 * @return lista di nomi di tutte le zone
	 */
	@Override
	public List<String> getAllZones() {
		return this.gameBoard.getRootZones().stream()
						.flatMap(root -> root.getChildZones().stream())
						.map(IZone::getName)
						.collect(Collectors.toList());
	}

	@Override
	public List<String> getChildZones(String rootZone){
		List<IZone> childZones = this.gameBoard.findZoneByName(rootZone).getChildZones();
		return childZones.stream()
						.map(IZone::getName)
						.toList();
	}

	@Override
	public int getZoneValue(String zone) {
		return this.gameBoard.getZoneValue(zone);
	}

	protected List<String> getParentZones(){
		return this.gameBoard.getRootZones().stream()
				.map(IZone::getName)
				.toList();
	}

	@Override
	public List<String> getNeighboursOf(String territoryName) {
		return this.gameBoard.getNeighbours(territoryName);
	}

	protected IZone findZoneByName(String territoryName) {
        return this.gameBoard.findZoneByName(territoryName);
    }

    protected boolean canMoveBetween(String toTerritory, String fromTerritory) {
        return this.gameBoard.canReach(toTerritory, fromTerritory);
    }
    
}
