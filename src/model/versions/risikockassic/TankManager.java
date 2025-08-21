package model.versions.risikockassic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.board.IZone;
import model.management.MapManager;
import model.management.TokenManager;
import model.players.IPlayer;
import model.utils.EnumToken;

public class TankManager extends TokenManager {

	private Boolean isReady;
	private final static int MIN_PLAYERS = 3;
	private final static int MAX_PLAYERS = 6;
	
	private static TankManager instance;
	
	private TankManager() {
		super();
		this.isReady = false;
	}
	
	public static TankManager getInstance() {
		if (instance == null) {
			instance = new TankManager();
		}
		return instance;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		checkInitialized();
		if (players == null || players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
			throw new IllegalArgumentException("Number of players must be between 3 and 6");
		}
		
		this.assignTokensToPlayers(players);
		this.initializeZones(MapManagerRisikoNew.getInstance().getAllTerritories());
		this.isReady = true;
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	/**
	 * Ottiene il numero di tank di un giocatore
	 * @param player il giocatore
	 * @return numero di tank posseduti
	 */
	public int getPlayerTanks(IPlayer player) {
		checkReady();
		if (player == null) {
			throw new IllegalArgumentException("Player cannot be null");
		}
		return super.getPlayerToken(player, EnumToken.TANK);
	}
	
	/**
	 * Ottiene il numero di tank in una zona
	 * @param zone la zona
	 * @return numero di tank nella zona
	 */
	public int getZoneTanks(IZone zone) {
		checkReady();
		return super.getZoneToken(zone, EnumToken.TANK);
	}
	
	public void resetInstance() {
		instance = null;
		this.isReady = false;
		super.resetTokenData();
	}
	
	@Override
	protected Set<EnumToken> getManagedTokens() {
		checkReady();
		return Set.of(EnumToken.TANK);
	}

	@Override
	protected Map<EnumToken, Integer> initTokensPerPlayer(int playerCount) {
		checkInitialized();
		int tanksPerPlayer = this.calculateTanksPerPlayer(playerCount);
		Map<EnumToken, Integer> tokens = new HashMap<>();
		tokens.put(EnumToken.TANK, tanksPerPlayer);
		return tokens;
	}

	@Override
	protected Map<EnumToken, Integer> initTokensPerZone(IZone zone) {
		checkInitialized();
		Map<EnumToken, Integer> tokens = new HashMap<>();
		tokens.put(EnumToken.TANK, 1);
		super.removePlayerToken(zone.getOwners().get(0), EnumToken.TANK, 1);
		return tokens; 
	}

	// TO DO Valutare implementazione interfaccia IMapManager
	@Override
	protected MapManager getMapManager() {
		return MapManagerRisikoNew.getInstance();
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
	
	/**
	 * Inizializza le zone con i tank. Da chiamare dopo che il board è stato creato.
	 * @param zones le zone del gioco
	 */
	private void initializeZones(List<IZone> zones) {
		super.assignTokensToZones(zones);
	}

}