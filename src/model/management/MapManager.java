package model.management;

import java.util.List;
import java.util.stream.Collectors;

import model.board.IBoardCreator;
import model.board.IGameBoard;
import model.board.IZone;
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
	
	/**
	 * Inizializza l'assegnamento delle zone ai giocatori.
	 */
	protected abstract void initPlayerZones(List<IPlayer> players);
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}
	
	@Override
	public void resetGame() {
		this.resetGameBoard();
	}
	
	/**
	 * Utility method per le sottoclassi
	 * Restituisce tutti i territori della mappa.
	 * 
	 * @return lista dei territori
	 */
	public List<IZone> getAllZones() {
		return this.gameBoard.getZones().stream()
				.flatMap(continent -> continent.getChildZones().stream())
				.collect(Collectors.toList());
	}
	
	protected IZone findZoneByName(String territoryName) {
        this.gameBoardCheck();
        return this.gameBoard.findZoneByName(territoryName);
    }

    protected List<String> getNeighbours(String territoryName) {
        this.gameBoardCheck();
        return this.gameBoard.getNeighbours(territoryName);
    }

    protected boolean canMoveBetween(String toTerritory, String fromTerritory) {
        this.gameBoardCheck();
        return this.gameBoard.canReach(toTerritory, fromTerritory);
    }
    
	/**
	 * Utility method per le sottoclassi
	 * Ottiene la mappa di gioco corrente. Se la mappa di gioco non è stata ancora
	 * inizializzata, lancia un'eccezione.
	 * 
	 * @return la mappa di gioco corrente
	 * @throws IllegalStateException se la mappa non è stata inizializzata
	 */
	protected IGameBoard getGameBoard() {
		this.gameBoardCheck();
		return this.gameBoard;
	} 
	
	/**
	 * Utility method per le sottoclassi
	 * Verifica se la mappa di gioco è stata inizializzata.
	 * 	 * @return true se la mappa di gioco è pronta, false altrimenti
	 */
	protected boolean isGameBoardReady() {
		this.gameBoardCheck();
		return this.gameBoard != null;
	}
    
    private void gameBoardCheck() {
        if (this.gameBoard == null) {
            throw new IllegalStateException("Game board has not been initialized.");
        }
    }
	
	private void resetGameBoard() {
		this.gameBoard = null;
	}
	
}
