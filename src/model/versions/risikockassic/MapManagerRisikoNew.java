package model.versions.risikockassic;

import java.util.List;
import java.util.Optional;

import model.board.IGameBoard;
import model.board.IZone;
import model.IPlayer;
import model.management.MapManager;
import model.utils.GameVersion;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class MapManagerRisikoNew extends MapManager {
	
	private static MapManagerRisikoNew instance;
	
	private IGameBoard gameBoard;
	private Boolean isReady;
	
	private MapManagerRisikoNew() {
		super(GameVersion.RISIKOCLASSIC);
		this.isReady = false;
	
	}
	
	public static MapManagerRisikoNew getInstance() {
		if (instance == null) {
			instance = new MapManagerRisikoNew();
		}
		return instance;
	}
	
	public void resetInstance() {
		instance = null;
		this.gameBoard = null;
		this.isReady = false;
	}

	@Override
	protected IGameBoard requestGameMap() {
        return BoardCreatorRisikoNew.getInstance().getMap();
	}    
	
	@Override
	protected void initPlayerZones(List<IPlayer> players) {
	}
	
	@Override
	public void initializeGame() {
		this.gameBoard = super.getGameBoard();
		if (this.gameBoard != null) {
			this.isReady = true;
		} else {
			this.isReady = false;
			throw new IllegalStateException("Game board is not initialized.");
		}
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	/**
	 * Restituisce tutti i continenti del Risiko Classico.
	 * 
	 * @return lista dei continenti
	 */
	public List<IZone> getAllContinents() {
		checkReady();
		return this.gameBoard.getZones();
	}
	
	/**
	 * Restituisce tutti i territori del Risiko Classico.
	 * 
	 * @return lista dei continenti
	 */
	public List<IZone> getAllTerritories() {
		checkReady();
		return this.gameBoard.getZones().stream()
				.flatMap(continent -> continent.getChildZones().stream())
				.toList();
	}
	
	/**
	 * Trova un territorio specifico per nome.
	 * 
	 * @param territoryName il nome del territorio da cercare
	 * @return il territorio trovato o null se non esiste
	 */
	public IZone findTerritoryByName(String territoryName) {
		checkReady();
		return this.gameBoard.findZoneByName(territoryName);
	}
	
	/**
	 * Trova il continente che contiene il territorio specificato.
	 * 
	 * @param territoryName il nome del territorio
	 * @return il continente che contiene il territorio
	 */
	public Optional<IZone> findContinentOfTerritory(String territoryName) {
		checkReady();
		return this.gameBoard.whereIsZone(territoryName);
	}
	
	/**
	 * Restituisce tutti i territori confinanti con quello specificato.
	 * 
	 * @param territoryName il nome del territorio
	 * @return lista dei nomi dei territori confinanti
	 */
	public List<String> getAdjacentTerritories(String territoryName) {
		checkReady();
		return this.gameBoard.getNeighbours(territoryName);
	}
	
	/**
	 * Controlla se è possibile spostare armate tra due territori.
	 * 
	 * @param fromTerritory il territorio di partenza
	 * @param toTerritory il territorio di destinazione
	 * @return true se lo spostamento è possibile, false altrimenti
	 */
	public boolean canMoveBetween(String fromTerritory, String toTerritory) {
		checkReady();
		return this.gameBoard.canReach(toTerritory, fromTerritory);
	}
	
	/**
	 * Restituisce il bonus di armate fornito dal controllo di un continente.
	 * 
	 * @param continentName il nome del continente
	 * @return il numero di armate bonus
	 */
	public Integer getContinentArmyBonus(String continentName) {
		checkReady();
		return this.gameBoard.getValue(continentName);
	}
	
	/**
	 * Restituisce il valore strategico di un territorio.
	 * 
	 * @param territoryName il nome del territorio
	 * @return il valore del territorio
	 */
	public Integer getTerritoryValue(String territoryName) {
		checkReady();
		return this.gameBoard.getValue(territoryName);
	}
	
	/**
	 * Controlla se il MapManager è pronto per l'uso.
	 */
	private void checkReady() {
		if (!this.isReady) {
			throw new IllegalStateException("MapManagerRisikoNew is not ready. Call initializeGame() first.");
		}
	}
	
}
