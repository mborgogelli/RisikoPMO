package model.versions.risikockassic.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.management.MapManager;
import model.players.IPlayer;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;
import model.versions.risikockassic.management.interfaces.IMapManagerRisikoNew;

public class MapManagerRisikoNew extends MapManager implements IMapManagerRisikoNew {
	
	//TODO centralizzare il check di esistenza del territorio nella fase di init r toglierlo dai metodi
	
	private Boolean isReady;
	private List<IPlayer> players;
	private final Map<IPlayer,List<String>> playerTerritories;
	private final Map<String, List<String>> continents;
	
	public MapManagerRisikoNew() {
		super(BoardCreatorRisikoNew.getInstance());
		this.isReady = false;
		this.playerTerritories = new HashMap<>();
		this.continents = new HashMap<>();
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.players = players;
		this.initPlayerZones(this.players);
		this.initContinentsMap();
		this.isReady = true;
}
	
	@Override
	public void resetGame() {
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
	public IPlayer getOwner(String zone) {
		checkIfExists(zone);
		return this.playerTerritories.entrySet().stream()
								.filter(playerZones -> playerZones.getValue().contains(zone))
								.findFirst()
								.get()
								.getKey();
	}
	
	@Override
	public void updateOwnership(IPlayer newOwner, String territory) {
		checkReady();
		checkIfExists(newOwner);
		checkIfExists(territory);
		this.playerTerritories.get(this.getOwner(territory)).remove(territory);
		this.playerTerritories.get(newOwner).add(territory);
	}

	@Override
	public Map<IPlayer, List<String>> getTerritoriesAssignment() {
		return Collections.unmodifiableMap(this.playerTerritories);
	}

	@Override
	public List<String> checkZoneCompletion(IPlayer player) {
		boolean isContinentComplete = false;
		List<String> myContinents = new ArrayList<>();
		List<String> playerTerritories = this.playerTerritories.get(player);
		for(String continent : this.continents.keySet()) {
			isContinentComplete = this.continents.get(continent).stream()
											.allMatch(territory -> playerTerritories.contains(territory));
			if(isContinentComplete) {
				myContinents.add(continent);
				isContinentComplete = false;
			}
		}
		return myContinents;
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
	
	private void initContinentsMap() {
		List<String> continents = super.getParentZones();
		for(String continent : continents) {
			this.continents.put(continent, super.getChildZones(continent));
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
		if (territory == null || territory == "" || !super.getAllZones().contains(territory)) {
			throw new IllegalArgumentException("Territory " + territory + " not found.");
		}
	}
	
	private void checkIfExists(IPlayer player) {
		if (player == null || this.players.isEmpty() || !this.players.contains(player)) {
			throw new IllegalArgumentException("Player " + player.toString() + " not found.");
		}
	}

	
}
