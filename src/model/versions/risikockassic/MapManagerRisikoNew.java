package model.versions.risikockassic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import model.board.IGameBoard;
import model.board.IZone;
import model.management.MapManager;
import model.players.IPlayer;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class MapManagerRisikoNew extends MapManager {
	
	private static MapManagerRisikoNew instance;
	
	private IGameBoard gameBoard;
	private Boolean isReady;
	private final Map<IPlayer,List<IZone>> playerTerritories;
	
	private MapManagerRisikoNew() {
		super();
		this.isReady = false;
		this.playerTerritories = new HashMap<>();
	
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
	public void initializeGame(List<IPlayer> players) {
		this.gameBoard = super.getGameBoard();
		if (this.gameBoard != null) {
			this.initPlayerZones(players);
			this.setPlayerTerritories(players);
			this.isReady = true;
		} else {
			this.isReady = false;
			throw new IllegalStateException("Game board is not initialized.");
		}
	}
	
	@Override
	protected void initPlayerZones(List<IPlayer> players) {
		List<IZone> territories = this.getTerritories();
		Collections.shuffle(territories);
		for (IZone territory : territories) {
			IPlayer player = players.get(territories.indexOf(territory) % players.size());
			territory.setOwner(player);
		}
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	public Map<IPlayer, List<IZone>> getPlayerTerritories(){
		checkReady();
		return this.playerTerritories;
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
		return this.getTerritories();
	}
	
	/**
	 * Controlla se due territori hanno lo stesso proprietario.
	 * 
	 * @param zone1 il primo territorio
	 * @param zone2 il secondo territorio
	 * @return true se hanno lo stesso proprietario, false altrimenti
	 */
	public Boolean isSameOwner(IZone zone1, IZone zone2) {
		checkReady();
		checkIfExists(zone1);
		checkIfExists(zone2);
		return zone1.getOwners().equals(zone2.getOwners());
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
	 * Restituisce tutti i territori posseduti da un giocatore specifico.
	 * 
	 * @param player il giocatore di cui si vogliono ottenere i territori
	 * @return lista dei territori posseduti dal giocatore
	 */
	public List<IZone> getTerritoriesOwnedBy(IPlayer player) {
		return this.getAllTerritories().stream()
				.filter(territory -> territory.getOwners().contains(player))
				.toList();
	}
	
	/**
	 * Restituisce tutti i territori della mappa.
	 * 
	 * @return lista dei territori
	 */
	private List<IZone> getTerritories() {
		return this.gameBoard.getZones().stream()
				.flatMap(continent -> continent.getChildZones().stream())
				.collect(Collectors.toList());
	}
	
	/**
	 * Controlla se il MapManager è pronto per l'uso.
	 */
	private void checkReady() {
		if (!this.isReady) {
			throw new IllegalStateException("MapManager must be initialized first.");
		}
	}
	
	private void checkIfExists(IZone territory) {
		if (territory == null || !this.getTerritories().contains(territory)) {
			throw new IllegalArgumentException("Territory " + territory.toString() + " not found.");
		}
	}
	
	private void setPlayerTerritories(List<IPlayer> players) {
	    for (IPlayer p : players) {
	        List<IZone> territories = this.getTerritories().stream()
	                .filter(territory -> territory.getOwners().contains(p))
	                .toList();
	        this.playerTerritories.put(p, territories);
	    }
	}
}
