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
import model.players.Player;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class MapManagerRisikoNew extends MapManager {
	
	private static MapManagerRisikoNew instance;
	
	private Boolean isReady;
	private final Map<IPlayer,List<IZone>> playerTerritories;
	
	private MapManagerRisikoNew() {
		super(BoardCreatorRisikoNew.getInstance());
		this.isReady = false;
		this.playerTerritories = new HashMap<>();
	}
	
	public static MapManagerRisikoNew getInstance() {
		if (instance == null) {
			instance = new MapManagerRisikoNew();
		}
		return instance;
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		if (super.isGameBoardReady()) {
			this.initPlayerZones(players);
			this.setPlayerTerritories(players);
			this.isReady = true;
		} else {
			this.isReady = false;
			throw new IllegalStateException("Game board has not been initialized.");
		}
	}
	
	@Override
	public void resetInstance() {
		instance = null;
		super.resetInstance();
		this.isReady = false;
	}
	
	@Override
	protected void initPlayerZones(List<IPlayer> players) {
		List<IZone> territories = super.getAllZones();
		Collections.shuffle(territories);
		for (IZone territory : territories) {
			IPlayer player = players.get(territories.indexOf(territory) % players.size());
			territory.setOwner(player);
		}
	}
	
	public Map<IPlayer, List<IZone>> getPlayerTerritories(){
		checkReady();
		return this.playerTerritories;
	}
	

	@Override
	public boolean canMoveBetween(IPlayer player, String toTerritory, String fromTerritory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IZone> getZonesOwnedBy(IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateZoneOwnership(IPlayer newOwner, IZone territory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IZone> getNeighboursNotOwnedBy(String territoryName, IPlayer player) {
		// TODO Auto-generated method stub
		return null;
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
	 * Restituisce tutti i territori posseduti da un giocatore specifico.
	 * 
	 * @param player il giocatore di cui si vogliono ottenere i territori
	 * @return lista dei territori posseduti dal giocatore
	 */
	public List<IZone> getTerritoriesOwnedBy(IPlayer player) {
		return super.getAllZones().stream()
				.filter(territory -> territory.getOwners().contains(player))
				.toList();
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
		if (territory == null || !super.getAllZones().contains(territory)) {
			throw new IllegalArgumentException("Territory " + territory.toString() + " not found.");
		}
	}
	
	private void setPlayerTerritories(List<IPlayer> players) {
	    for (IPlayer p : players) {
	        List<IZone> territories = super.getAllZones().stream()
	                .filter(territory -> territory.getOwners().contains(p))
	                .toList();
	        this.playerTerritories.put(p, territories);
	    }
	}


	/**
	 * Ottiene tutte le zone della mappa di gioco che appartengono ad un dato Player.
	 * Se la mappa di gioco non è stata ancora inizializzata, lancia un'eccezione.
	 * 
	 * @param player
	 * @return
	 */
	/*protected List<IZone> getZonesByPlayer(IPlayer player) {
		super.gameBoardCheck();
		return this.gameBoard.getZones().stream()
				.filter(zone -> zone.isControlledBy(player))
				.toList();
	}*/
}
