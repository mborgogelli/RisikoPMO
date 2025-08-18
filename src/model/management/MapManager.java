package model.management;

import java.util.List;

import model.IPlayer;
import model.board.IGameBoard;
import model.board.IZone;
import model.utils.GameVersion;

/* Classe astratta per la gestione delle mappe di gioco.
 * Fornisce metodi per ottenere la mappa di gioco in base alla versione del gioco.
 * 
 */
public abstract class MapManager implements IManager {
	
	private IGameBoard gameBoard;
	
	protected MapManager(GameVersion gameVersion) {
		if (gameVersion == null || !gameVersionIsValid(gameVersion)) {
			throw new IllegalArgumentException("Invalid game version: " + gameVersion);
		}
		//this.gameBoard = this.getGameBoard();
	}
	
	/**
	 * Richiede la mappa di gioco per la versione specificata.
	 */
	protected abstract IGameBoard requestGameMap();
	
	/**
	 * Inizializza l'assegnamento delle zone ai giocatori.
	 */
	protected abstract void initPlayerZones(List<IPlayer> players);
	
	/**
	 * Ottiene la versione del gioco corrente.
	 * Se la mappa di gioco non è stata ancora inizializzata, lancia un'eccezione.
	 * 
	 * @return la versione del gioco corrente
	 * @throws IllegalStateException se la mappa non è stata inizializzata
	 */
	public GameVersion getGameVersion() {
		this.gameBoardCheck();
		return this.gameBoard.getGameVersion();
	}
	
	/**
	 * Ottiene la mappa di gioco in base alla versione del gioco.
	 * Se la mappa è già stata inizializzata per la versione richiesta, la restituisce.
	 * Altrimenti, richiede una nuova mappa per la versione specificata.
	 * 
	 * @param gameversion la versione del gioco
	 * @return la mappa di gioco per la versione specificata
	 */
	protected IGameBoard getGameBoard() {
		this.gameBoard = this.requestGameMap();
		return this.gameBoard;
	}
	
	/**
	 * Ottiene tutte le zone della mappa di gioco che appartengono ad un dato Player.
	 * Se la mappa di gioco non è stata ancora inizializzata, lancia un'eccezione.
	 * 
	 * @param player
	 * @return
	 */
	protected List<IZone> getZonesByPlayer(IPlayer player) {
		this.gameBoardCheck();
		return this.gameBoard.getZones().stream()
				.filter(zone -> zone.isControlledBy(player))
				.toList();
	}
	
	/**
	 * Verifica se la versione del gioco è valida.
	 * 
	 * @param gameVersion la versione del gioco da verificare
	 * @return true se la versione è valida, false altrimenti
	 */
	private boolean gameVersionIsValid(GameVersion gameVersion) {
		Boolean isValid = false;
		for (GameVersion version : GameVersion.values()) {
			if (version == gameVersion) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	private void gameBoardCheck() {
		if (this.gameBoard == null) {
			throw new IllegalStateException("Game board has not been initialized. Please request a game map first.");
		}
	}
	

}
