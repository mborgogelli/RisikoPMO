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
	
	private static TankManager instance;
	
	protected TankManager() {
		super();
		this.isReady = false;
	}
	
	public TankManager getInstance() {
		if (instance == null) {
			instance = new TankManager();
		}
		return instance;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		if (players == null || players.size() < 3 || players.size() > 6) {
			throw new IllegalArgumentException("Number of players must be between 3 and 6");
		}
		
		this.assignTokensToPlayers(players);
		
		this.isReady = true;
	}
	
	/**
	 * Inizializza le zone con i tank. Da chiamare dopo che il board è stato creato.
	 * @param zones le zone del gioco
	 */
	public void initializeZones(List<IZone> zones) {
		if (!isReady) {
			throw new IllegalStateException("Manager must be initialized with players first");
		}
		assignTokensToZones(zones);
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
		if (zone == null) {
			throw new IllegalArgumentException("Zone cannot be null");
		}
		return super.getZoneToken(zone, EnumToken.TANK);
	}
	
	@Override
	protected Set<EnumToken> getManagedTokens() {
		return Set.of(EnumToken.TANK);
	}
	@Override
	protected void assignTokensToZones(List<IZone> zones) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<EnumToken, Integer> calculateInitialTokensPerPlayer(int playerCount) {
		int tanksPerPlayer = calculateTanksPerPlayer(playerCount);
		Map<EnumToken, Integer> tokens = new HashMap<>();
		tokens.put(EnumToken.TANK, tanksPerPlayer);
		return tokens;
	}

	@Override
	protected Map<EnumToken, Integer> calculateInitialTokensPerZone(IZone zone) {
		// Nel Risiko classico, le zone non iniziano con tank automatici
		// I tank vengono piazzati esplicitamente durante la fase di setup
		return new HashMap<>(); 
	}

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

}
