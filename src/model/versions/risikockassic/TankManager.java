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
		
	/**
	 * Rimuove un certo numero di tank da un giocatore.
	 * Imposta a 0 il numero di tank posseduti se il numero da rimuovere è maggiore
	 * del numero di tank posseduti.
	 * 
	 * @param player
	 * @param amount
	 */
	public void removeTanksFromPlayer(IPlayer player, int amount) {
		checkReady();
		int newAmount = this.getPlayerTanks(player) - amount;
		this.availableTanks.put(player, (newAmount > 0)? newAmount: 0);
	}
	
	/**
	 * Ottiene il numero di tank di un giocatore
	 * 
	 * @param player il giocatore
	 * @return numero di tank posseduti
	 */
	public int getPlayerTanks(IPlayer player) {
		checkReady();
		return this.availableTanks.getOrDefault(player, 0);
	}
	
	/**
	 * Ottiene il numero di tank in una zona
	 * 
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
		checkIfPlayerHasTank(player, amount);
		checkIfPlayerOwnsZone(player, zone);
		int zoneAmount = this.deployedTank.get(zone) - amount;
		this.deployedTank.put(zone, zoneAmount);
		int playerAmount = this.availableTanks.get(player) - amount;
		this.availableTanks.put(player, playerAmount);
	}
	
	public void moveTanks(IPlayer player, IZone fromZone, IZone toZone, int amount) {
		checkReady();
	}
	
	@Override
	protected void resetTokenData() {
		// TODO Auto-generated method stub
		
	}

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
			int newAmount = this.getPlayerTanks(p) - deployed;
			this.availableTanks.put(p, newAmount);
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
	
	/** Controlla se un giocatore può dispiegare un certo numero di tank
	 *
	 * @param player il giocatore
	 * @param amount il numero di tank da dispiegare
	 * 
	 */
	private void checkIfPlayerHasTank(IPlayer player, int amount) {
		 if (this.availableTanks.get(player) - amount < 0) {
			 throw new IllegalStateException("Player " + player + " does not have enough tanks to deploy " + amount);
		 }
	}
	
	/** Controlla se un giocatore può spostare un certo numero di tank da una zona
	 * 
	 * @param player il giocatore
	 * @param fromZone la zona di partenza
	 * @param amount il numero di tank da spostare
	 * @return true se il giocatore può spostare i tank, false altrimenti
	 */
	private void checkDeployedTanksAfterMove(IPlayer player, IZone fromZone, int amount) {
		int current = this.deployedTank.get(fromZone);
		if(amount <= 0 || (current - amount < 1)) {
			throw new IllegalArgumentException("Cannot move " + amount + "tanks. Amount must be greater than 0 and less than or equal to " + (current - 1));
		}
	}
	
	/** Controlla se un giocatore può spostare tank tra due zone
	 * 
	 * @param player il giocatore
	 * @param fromZone la zona di partenza
	 * @param toZone la zona di arrivo
	 * @return true se il giocatore può spostare i tank, false altrimenti
	 */
	private void checkIfPlayerCanMoveBetween(IPlayer player, IZone fromZone, IZone toZone) {
		MapManagerRisikoNew mapManager = getMapManager();
		if(!mapManager.canMoveBetween(fromZone.getName(), toZone.getName()) || 
		   !mapManager.getTerritoriesOwnedBy(player).contains(fromZone) ||
		   !mapManager.getTerritoriesOwnedBy(player).contains(toZone)) {
			
				throw new IllegalStateException("Player " + player + " cannot move tanks between " + fromZone.getName() + " and " + toZone.getName());
		};
	}
	
	private void checkIfPlayerOwnsZone(IPlayer player, IZone zone) {
		MapManagerRisikoNew mapManager = getMapManager();
		if (!mapManager.getTerritoriesOwnedBy(player).contains(zone)) {
			throw new IllegalStateException("Player " + player + " does not own zone " + zone.getName());
		}
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