package model.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.board.IZone;
import model.management.interfaces.IManager;
import model.players.IPlayer;
import model.utils.EnumToken;

/**
 * TokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class TokenManager implements IManager{
	
	private Map<IZone,Map<EnumToken,Integer>> tokenDeployed;
	private Map<IPlayer,Map<EnumToken,Integer>> tokenOwned;
	
	protected TokenManager() {
	    this.tokenDeployed = new HashMap<>();
	    this.tokenOwned = new HashMap<>();
	}

	/**
	 * Restituisce i tipi di token gestiti da questo manager
	 * @return Set dei token gestiti
	 */
	protected abstract Set<EnumToken> getManagedTokens();
	
	/**
	 * Calcola i token iniziali per giocatore basandosi sul numero di giocatori
	 * @param playerCount numero di giocatori
	 * @return Map con i token per tipo
	 */
	protected abstract Map<EnumToken, Integer> calculateInitialTokensPerPlayer(int playerCount);
	
	/**
	 * Determina i token iniziali per zona
	 * @param zone la zona
	 * @return Map con i token per tipo, o mappa vuota se la zona non ha token iniziali
	 */
	protected abstract Map<EnumToken, Integer> calculateInitialTokensPerZone(IZone zone);
	
	/**
	 * Restituisce il MapManager specifico per questa versione del gioco
	 * @return il MapManager da utilizzare per le validazioni territoriali
	 */
	protected abstract MapManager getMapManager();
	
	/**
	 * Inizializza le zone con i token appropriati
	 * @param zones lista delle zone
	 */
	protected void assignTokensToZones(List<IZone> zones) {
		for (IZone zone : zones) {
			Map<EnumToken, Integer> initialTokens = this.calculateInitialTokensPerZone(zone);
			for (Map.Entry<EnumToken, Integer> entry : initialTokens.entrySet()) {
				this.addZoneToken(zone, entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * Assegna i token iniziali ai giocatori
	 * @param players lista dei giocatori
	 */
	protected void assignTokensToPlayers(List<IPlayer> players) {
		Map<EnumToken, Integer> tokensPerPlayer = this.calculateInitialTokensPerPlayer(players.size());
		
		for (IPlayer player : players) {
			for (Map.Entry<EnumToken, Integer> entry : tokensPerPlayer.entrySet()) {
				this.addPlayerToken(player, entry.getKey(), entry.getValue());
			}
		}
	}
	
	protected Integer getZoneToken(IZone zone, EnumToken tokenType) {
	    return tokenDeployed.getOrDefault(zone, Map.of()).getOrDefault(tokenType, 0);
	}

	protected void addZoneToken(IZone zone, EnumToken tokenType, Integer amount) {
	    Integer current = this.getZoneToken(zone, tokenType);
	    this.setZoneToken(zone, tokenType, current + amount);
	}

	protected void removeZoneToken(IZone zone, EnumToken tokenType, Integer amount) {
	    Integer current = getZoneToken(zone, tokenType);
	    if (current >= amount) {
	        this.setZoneToken(zone, tokenType, current - amount);
	    } else {
	    	this.setZoneToken(zone, tokenType, 0);
	    }
	}
	
	protected Integer getPlayerToken(IPlayer player, EnumToken tokenType) {
	    return tokenOwned.getOrDefault(player, Map.of()).getOrDefault(tokenType, 0);
	}

	protected void addPlayerToken(IPlayer player, EnumToken tokenType, Integer amount) {
	    Integer current = this.getPlayerToken(player, tokenType);
	    this.setPlayerToken(player, tokenType, current + amount);
	}

	protected void removePlayerToken(IPlayer player, EnumToken tokenType, Integer amount) {
	    Integer current = this.getPlayerToken(player, tokenType);
	    if (current >= amount) {
	        this.setPlayerToken(player, tokenType, current - amount);
	    } else {
	        this.setPlayerToken(player, tokenType, 0);
	    }
	}
	
	/**
	 * Imposta il numero di token in una zona
	 * @param zone la zona
	 * @param tokenType il tipo di token
	 * @param count il numero di token da impostare
	 */
	private void setZoneToken(IZone zone, EnumToken tokenType, Integer count) {
        this.tokenDeployed.computeIfAbsent(zone, z -> new HashMap<>()).put(tokenType, count);
	}
	
	/**
	 * Imposta il numero di token posseduti da un giocatore
	 * @param player il giocatore
	 * @param tokenType il tipo di token
	 * @param count il numero di token da impostare
	 */
	private void setPlayerToken(IPlayer player, EnumToken tokenType, Integer count) {
	    this.tokenOwned.computeIfAbsent(player, p -> new HashMap<>()).put(tokenType, count);
	}
	
	/**
	 * Verifica se un giocatore ha un numero sufficiente di token di un certo tipo.
	 * 
	 * @param player il giocatore da verificare
	 * @param tokenType il tipo di token da controllare
	 * @param required il numero minimo di token richiesti
	 * @return true se il giocatore ha almeno il numero richiesto di token, false altrimenti
	 */
	protected Boolean validatePlayerHasTokens(IPlayer player, EnumToken tokenType, int required) {
		int available = getPlayerToken(player, tokenType);
		return available >= required;
	}

}
