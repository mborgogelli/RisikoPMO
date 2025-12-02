package model.versions.risikockassic.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.management.TokenManager;
import model.players.IPlayer;

public class TankManager extends TokenManager {

	private Boolean isReady;
	
	private final Map<String, Integer> deployedTank;
	private final Map<IPlayer, Integer> availableTanks;
	
	public TankManager() {
		super();
		this.isReady = false;
		this.availableTanks = new HashMap<>();
		this.deployedTank = new HashMap<>();
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	@Override
	public void resetGame() {
		this.isReady = false;
		this.resetTokenData();
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		checkInitialized();
		this.initTokensPerPlayer(players);
		this.initTokensPerZone(super.getAllZones());
		this.isReady = true;
	}
	
	/**
	 * Ottiene il numero di tank di un giocatore
	 * 
	 * @param player il giocatore
	 * @return numero di tank posseduti
	 */
	@Override
	public int getPlayerToken(IPlayer player) {
		checkReady();
		return this.availableTanks.getOrDefault(player, 0);
	}
	
	/**
	 * Ottiene il numero di tank in una zona
	 * 
	 * @param zone la zona
	 * @return numero di tank nella zona
	 */
	@Override
	public int getZoneToken(String zone) {
		checkReady();
		return this.deployedTank.getOrDefault(zone, 0);
	}
	
	@Override
	public Map<String, Integer> getDeployedPerZone(IPlayer player){
		return this.deployedTank.keySet().stream()
										.filter(zone -> super.getZonesOwnedBy(player).contains(zone))
										.collect(Collectors.toMap(zone -> zone, zone -> this.deployedTank.get(zone)));
		
	}
	
	@Override
	public int getTotalDeployed(IPlayer player) {
		return this.getDeployedPerZone(player).values().stream()
													.reduce(0, Integer::sum);	
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
	@Override
	public void deployToken(IPlayer player, String zone, int amount) {
		checkReady();
		checkIfPlayerHasTank(player, amount);
		checkIfPlayerOwnsZone(player, zone);
		addTanksToZone(zone, amount);
		removeTanksFromPlayer(player, amount);
	}
	
	@Override
	public void moveToken(IPlayer player, String toZone, String fromZone, int amount) {
		checkReady();
		checkIfPlayerCanMoveBetween(player, toZone, fromZone);
		checkDeployedTanksAfterMove(player, fromZone, amount);
		removeTanksFromZone(fromZone, amount);
		addTanksToZone(toZone, amount);
	}
	
	@Override
	protected void resetTokenData() {
		checkInitialized();
		this.availableTanks.clear();
		this.deployedTank.clear();
	}

	private void initTokensPerPlayer(List<IPlayer> players) {
		checkInitialized();
		int tanksPerPlayer = this.calculateTanksPerPlayer(players.size());
		players.stream().forEach(p -> this.availableTanks.put(p, tanksPerPlayer));
	}

	private void initTokensPerZone(List<String> territories) {
		checkInitialized();
		territories.stream().forEach(z -> this.deployedTank.put(z, 1));
		for (IPlayer p : this.availableTanks.keySet()) {
			int deployed = super.getZonesOwnedBy(p).size();
			int newAmount = this.availableTanks.get(p) - deployed;
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
	

	/**
	 * Rimuove un certo numero di tank da un giocatore.
	 * Imposta a 0 il numero di tank posseduti se il numero da rimuovere è maggiore
	 * del numero di tank posseduti.
	 * 
	 * @param player
	 * @param amount
	 */
	private void removeTanksFromPlayer(IPlayer player, int amount) {
		int newAmount = this.getPlayerToken(player) - amount;
		this.availableTanks.put(player, (newAmount > 0)? newAmount: 0);
	}
	
	/**
	 * Rimuove un certo numero di tank da una zona.
	 * Imposta a 0 il numero di tank nella zona se il numero da rimuovere è maggiore
	 * del numero di tank presenti nella zona.
	 * 
	 * @param zone
	 * @param amount
	 */
	private void removeTanksFromZone(String zone, int amount) {
		int newAmount = this.getZoneToken(zone) - amount;
		this.deployedTank.put(zone, (newAmount > 0)? newAmount: 0);
	}
	
	private void addTanksToZone(String zone, int amount) {
		int newAmount = this.getZoneToken(zone) + amount;
		this.deployedTank.put(zone, newAmount);
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
	 * Verifica che il numero di tank da spostare sia maggiore di 0 e che nella zona di partenza rimanga almeno 1 tank
	 * 
	 * @param player il giocatore
	 * @param fromZone la zona di partenza
	 * @param amount il numero di tank da spostare
	 * @return true se il giocatore può spostare i tank, false altrimenti
	 */
	private void checkDeployedTanksAfterMove(IPlayer player, String fromZone, int amount) {
		int current = this.deployedTank.get(fromZone);
		if(amount <= 0 || (current - amount < 1)) {
			throw new IllegalArgumentException("Cannot move " + amount + "tanks. Amount must be greater than 0 and less than or equal to " + (current - 1));
		}
	}
	
	/** Controlla se un giocatore può spostare tank tra due zone
	 *  Verifica che le due zone siano collegate e che siano di proprietà dello stesso giocatore
	 * 
	 * @param player il giocatore
	 * @param fromZone la zona di partenza
	 * @param toZone la zona di arrivo
	 * @return true se il giocatore può spostare i tank, false altrimenti
	 */
	private void checkIfPlayerCanMoveBetween(IPlayer player, String toZone, String fromZone) {
		super.canMoveBetween(player, toZone, fromZone);
	}
	
	private void checkIfPlayerOwnsZone(IPlayer player, String zone) {
		if (!super.getZonesOwnedBy(player).contains(zone)) {
			throw new IllegalStateException("Player " + player + " does not own zone " + zone);
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