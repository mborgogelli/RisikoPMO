package model.versions.risikockassic;

import java.util.List;
import java.util.Optional;

import model.board.IGameBoard;
import model.board.IZone;
import model.management.MapManager;
import model.utils.GameVersion;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class MapManagerRisikoNew extends MapManager {
	
	private static MapManagerRisikoNew instance;
	
	private IGameBoard gameBoard;
	private Boolean isReady;
	
	private MapManagerRisikoNew() {
		super(GameVersion.RISIKOCLASSIC);
		this.isReady = false;
	
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
	protected void initPlayerZones() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initializeGame() {
		this.gameBoard = super.getGameBoard();
		if (this.gameBoard != null) {
			this.isReady = true;
		} else {
			this.isReady = false;
			throw new IllegalStateException("Game board is not initialized.");
		}
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	/**
	 * Restituisce tutti i continenti del Risiko Classico.
	 * 
	 * @return lista dei continenti
	 */
	public List<IZone> getAllContinents() {
		return this.gameBoard.getZones();
	}
	
	/**
	 * Trova un territorio specifico per nome.
	 * 
	 * @param territoryName il nome del territorio da cercare
	 * @return il territorio trovato o null se non esiste
	 */
	public IZone findTerritoryByName(String territoryName) {
		return this.gameBoard.findZoneByName(territoryName);
	}
	
	/**
	 * Trova il continente che contiene il territorio specificato.
	 * 
	 * @param territoryName il nome del territorio
	 * @return il continente che contiene il territorio
	 */
	public Optional<IZone> findContinentOfTerritory(String territoryName) {
		return this.gameBoard.whereIsZone(territoryName);
	}
	
	/**
	 * Restituisce tutti i territori confinanti con quello specificato.
	 * 
	 * @param territoryName il nome del territorio
	 * @return lista dei nomi dei territori confinanti
	 */
	public List<String> getAdjacentTerritories(String territoryName) {
		return this.gameBoard.getNeighbours(territoryName);
	}
	
	/**
	 * Controlla se un territorio può attaccare un altro territorio.
	 * 
	 * @param attackingTerritory il territorio che attacca
	 * @param defendingTerritory il territorio che difende
	 * @return true se l'attacco è possibile, false altrimenti
	 */
	public boolean canAttackTerritory(String attackingTerritory, String defendingTerritory) {
		return this.gameBoard.canReach(defendingTerritory, attackingTerritory);
	}
	
	/**
	 * Controlla se è possibile spostare armate tra due territori.
	 * 
	 * @param fromTerritory il territorio di partenza
	 * @param toTerritory il territorio di destinazione
	 * @return true se lo spostamento è possibile, false altrimenti
	 */
	public boolean canMoveArmiesBetween(String fromTerritory, String toTerritory) {
		return this.gameBoard.canReach(toTerritory, fromTerritory);
	}
	
	/**
	 * Restituisce il bonus di armate fornito dal controllo di un continente.
	 * 
	 * @param continentName il nome del continente
	 * @return il numero di armate bonus
	 */
	public Integer getContinentArmyBonus(String continentName) {
		return this.gameBoard.getValue(continentName);
	}
	
	/**
	 * Restituisce il valore strategico di un territorio.
	 * 
	 * @param territoryName il nome del territorio
	 * @return il valore del territorio
	 */
	public Integer getTerritoryValue(String territoryName) {
		return this.gameBoard.getValue(territoryName);
	}

}
