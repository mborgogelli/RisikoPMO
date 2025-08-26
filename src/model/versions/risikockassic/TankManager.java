package model.versions.risikockassic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.board.IZone;
import model.management.TokenManager;
import model.players.IPlayer;
import model.utils.EnumToken;

public class TankManager extends TokenManager {

	private Boolean isReady;
	//i valori MIN e MAX non dovrebbero essere qui
	private final static int MIN_PLAYERS = 3; 
	private final static int MAX_PLAYERS = 6;
	
	private static TankManager instance;
	private final Map<IZone, Integer> deployedTank;
	private final Map<IPlayer, Integer> availableTanks;
	
	private TankManager() {
		super();
		this.isReady = false;
		this.availableTanks = new HashMap<>();
		this.deployedTank = new HashMap<>();
	}
	
	public static TankManager getInstance() {
		if (instance == null) {
			instance = new TankManager();
		}
		return instance;
	}
	
	public void resetInstance() {
		instance = null;
		this.isReady = false;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		checkInitialized();
		if (players == null || players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
			throw new IllegalArgumentException("Number of players must be between 3 and 6");
		}
		this.initTokensPerZone(this.getMapManager().getAllTerritories());
		this.initTokensPerPlayer(players);
		this.isReady = true;
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
		

	public void removeTanksFromPlayer(IPlayer player, int amount) {
		int currentTanks = this.getPlayerTanks(player);
		this.availableTanks.put(player, currentTanks - amount);
	}
	
	/**
	 * Ottiene il numero di tank di un giocatore
	 * @param player il giocatore
	 * @return numero di tank posseduti
	 */
	public int getPlayerTanks(IPlayer player) {
		checkReady();
		return this.availableTanks.getOrDefault(player, 0);
	}
	
	/**
	 * Ottiene il numero di tank in una zona
	 * @param zone la zona
	 * @return numero di tank nella zona
	 */
	public int getZoneTanks(IZone zone) {
		checkReady();
		return this.deployedTank.getOrDefault(zone, 0);
	}
	
	@Override
	public Set<EnumToken> getManagedTokens() {
		checkReady();
		return Set.of(EnumToken.TANK);
	}
	
	/**
	 * Esegue il dispiegamento di tank in una zona per un giocatore.
	 * 
	 * @param player il giocatore che dispiega i tank
	 * @param zone la zona in cui i tank vengono dispiegati
	 * @param amount il numero di tank da dispiegare
	 * @throws IllegalStateException se il gioco non è pronto o il giocatore non possiede abbastanza tank
	 * @throws IllegalArgumentException se la zona non è di proprietà del giocatore
	 * 
	 */
	public void deployTanks(IPlayer player, IZone zone, int amount) {
		checkReady();
		if (!zone.getOwners().contains(player)) {
			throw new IllegalStateException("Player " + player + " does not own zone " + zone.getName());
		}
	}
	
	public void moveTanks(IPlayer player, IZone fromZone, IZone toZone, int amount) {
		checkReady();
	}
	
	@Override
	protected void resetTokenData() {
		// TODO Auto-generated method stub
		
	}

	// TO DO Valutare implementazione interfaccia IMapManager
	@Override
	protected MapManagerRisikoNew getMapManager() {
		return MapManagerRisikoNew.getInstance();
	}
	
	private void initTokensPerPlayer(List<IPlayer> players) {
		int tanksPerPlayer = this.calculateTanksPerPlayer(players.size());
		players.stream().forEach(p -> this.availableTanks.put(p, tanksPerPlayer));
	}

	private void initTokensPerZone(List<IZone> territories) {
		territories.stream().forEach(z -> this.deployedTank.put(z, 1));
		for (IPlayer p : this.availableTanks.keySet()) {
			int deployed = this.getMapManager().getTerritoriesOwnedBy(p).size(); 
			this.removeTanksFromPlayer(p, deployed);
		}
	}

	/**
	 * Calcola il numero di tank per giocatore basandosi sul numero di giocatori
	 * @param playerCount numero di giocatori
	 * @return numero di tank per giocatore
	 */
	private int calculateTanksPerPlayer(int playerCount) {
		switch(playerCount) {
			case 3: return 35;
			case 4: return 30;
			case 5: return 25;
			case 6: return 20;
			default: throw new IllegalArgumentException("Invalid number of players: " + playerCount);
		}
	}
	
	private Boolean playerCanMoveBetween(IPlayer player, IZone fromZone, IZone toZone) {
		checkReady();
		MapManagerRisikoNew mapManager = getMapManager();
		return mapManager.canMoveBetween(fromZone.getName(), toZone.getName()) && 
			   mapManager.getTerritoriesOwnedBy(player).contains(fromZone) &&
			   mapManager.getTerritoriesOwnedBy(player).contains(toZone);
	}
	
	/**
	 * Controlla se il gioco è già stato inizializzato.
	 * Se sì, lancia un'eccezione IllegalStateException.
	 */
	private void checkReady() {
		if (!isReady) {
			throw new IllegalStateException("TokenManager must be initialized before use.");
		}
	}
	
	private void checkInitialized() {
		if (this.isReady) {
			throw new IllegalStateException("TokenManager is already initialized.");
		}
	}


}