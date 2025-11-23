package model.versions.risikockassic.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.board.IZone;
import model.management.MapManager;
import model.players.IPlayer;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;
import model.versions.risikockassic.interfaces.IMapManagerRisikoNew;

public class MapManagerRisikoNew extends MapManager implements IMapManagerRisikoNew {
	
	private Boolean isReady;
	private List<IPlayer> players;
	private final Map<IPlayer,List<String>> playerTerritories;
	
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
		this.players = players;
		this.initPlayerZones(this.players);
		this.isReady = true;
}
	
	@Override
	public void resetGame() {
		// TODO verifica il metodo clear() per la lista di players
		this.players = null;
		this.playerTerritories.clear();
		this.isReady = false;
	}
	
	@Override
	public boolean canMoveBetween(IPlayer player, String toTerritory, String fromTerritory) {
		checkReady();
		checkIfExists(player);
		checkIfExists(toTerritory);
		checkIfExists(fromTerritory);
		return super.canMoveBetween(toTerritory, fromTerritory) && this.isSameOwner(player, toTerritory, fromTerritory);
	}

	@Override
	public List<String> getZonesOwnedBy(IPlayer player) {
		this.checkReady();
		this.checkIfExists(player);
		return this.playerTerritories.get(player);
	}

	@Override
	public void updateOwnership(IPlayer newOwner, String territory) {
		checkReady();
		checkIfExists(newOwner);
		checkIfExists(territory);
	}

	@Override
	public List<IZone> getNeighboursOwnedBy(String territoryName, IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void initPlayerZones(List<IPlayer> players) {
		this.initializeMap(players);
		List<String> territories = super.getAllZones();
		Collections.shuffle(territories);
		for (String territory : territories) {
			IPlayer player = players.get(territories.indexOf(territory) % players.size());
			this.playerTerritories.get(player).add(territory);
		}
	}
	
	private void initializeMap(List<IPlayer> players) {
		for (IPlayer player : players) {
			this.playerTerritories.put(player, new ArrayList<String>());
		}
	}
	
	/**
	 * Controlla se due territori hanno lo stesso proprietario.
	 * 
	 * @param zone1 il primo territorio
	 * @param zone2 il secondo territorio
	 * @return true se hanno lo stesso proprietario, false altrimenti
	 */
	private Boolean isSameOwner(IPlayer player, String zone1, String zone2) {
		checkReady();
		checkIfExists(zone1);
		checkIfExists(zone2);
		return this.playerTerritories.get(player).containsAll(List.of(zone1, zone2));
	}

	/**
	 * Controlla se il MapManager è pronto per l'uso.
	 */
	private void checkReady() {
		if (!this.isReady) {
			throw new IllegalStateException("MapManager must be initialized first.");
		}
	}
	
	private void checkIfExists(String territory) {
		if (territory == null || !super.getAllZones().contains(territory)) {
			throw new IllegalArgumentException("Territory " + territory.toString() + " not found.");
		}
	}
	
	private void checkIfExists(IPlayer player) {
		if (player == null || this.players.isEmpty() || !this.players.contains(player)) {
			throw new IllegalArgumentException("Player " + player.toString() + " not found.");
		}
	}

	@Override
	public Map<IPlayer, List<String>> getTerritoriesAssignment() {
		return Collections.unmodifiableMap(this.playerTerritories);
	}
	
}
