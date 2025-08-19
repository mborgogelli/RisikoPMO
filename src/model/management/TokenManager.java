package model.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.board.IZone;
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

	protected abstract void assignTokensToZones(List<IZone> zones);
	
	protected abstract void assignTokensToPlayers(List<IPlayer> players);
	
	protected Map<IZone,Map<EnumToken,Integer>> getTokenDeployed(){
		return this.tokenDeployed;
	}
	
	protected Map<IPlayer,Map<EnumToken,Integer>> getPlayerToken(){
		return this.tokenOwned;
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
	
	private void setZoneToken(IZone zone, EnumToken tokenType, Integer count) {
        tokenDeployed.computeIfAbsent(zone, z -> new HashMap<>()).put(tokenType, count);
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
	
	private void setPlayerToken(IPlayer player, EnumToken tokenType, Integer count) {
	    tokenOwned.computeIfAbsent(player, p -> new HashMap<>()).put(tokenType, count);
	}

}
