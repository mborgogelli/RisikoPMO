package model.versions.risikockassic.managers;

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
import model.versions.risikockassic.interfaces.IMapManagerRisikoNew;

public class MapManagerRisikoNew extends MapManager implements IMapManagerRisikoNew {
	
	private Boolean isReady;
	private List<IPlayer> players;
	private final Map<IPlayer,List<IZone>> playerTerritories;
	
	public MapManagerRisikoNew() {
		super(BoardCreatorRisikoNew.getInstance());
		this.isReady = false;
		this.playerTerritories = new HashMap<>();
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		if (super.isGameBoardReady()) {
			this.players = players;
			this.initPlayerZones(this.players);
			this.setPlayerTerritories(this.players);
			this.isReady = true;
		} else {
			this.isReady = false;
			throw new IllegalStateException("Game board has not been initialized.");
		}
	}
	
	@Override
	public void resetGame() {
		// TODO verifica il metodo clear() per la lista di players
		this.players = null;
		this.playerTerritories.clear();
		super.resetGame();
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
	@Override
	public List<IZone> getAllZones() {
		//checkReady();
		return this.playerTerritories.values().stream()
									 .flatMap(List::stream)
									 .collect(Collectors.toList());
	}
	
	@Override
	public boolean canMoveBetween(IPlayer player, IZone toTerritory, IZone fromTerritory) {
		checkReady();
		checkIfExists(player);
		checkIfExists(toTerritory);
		return super.canMoveBetween(toTerritory.getName(), fromTerritory.getName()) && this.isSameOwner(toTerritory, fromTerritory);
	}

	@Override
	public List<IZone> getZonesOwnedBy(IPlayer player) {
		this.checkReady();
		this.checkIfExists(player);
		return this.playerTerritories.get(player);
	}

	@Override
	public void updateOwnership(IPlayer newOwner, IZone territory) {
		checkReady();
		checkIfExists(newOwner);
		checkIfExists(territory);
	}

	@Override
	public List<IZone> getNeighboursOwnedBy(IZone territoryName, IPlayer player) {
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
	private Boolean isSameOwner(IZone zone1, IZone zone2) {
		checkReady();
		checkIfExists(zone1);
		checkIfExists(zone2);
		return zone1.getOwners().equals(zone2.getOwners());
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
	
	private void checkIfExists(IPlayer player) {
		if (player == null || this.players.isEmpty() || !this.players.contains(player)) {
			throw new IllegalArgumentException("Player " + player.toString() + " not found.");
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
}
