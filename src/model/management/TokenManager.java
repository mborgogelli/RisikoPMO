package model.management;

import java.util.Map;

import model.board.IZone;

public abstract class TokenManager implements IManager{
	
	private Map<IZone, Integer> tokens;
	
	protected abstract void assignTokensToZones();
	
	protected abstract void assignTokensToPlayers();
	
	protected Integer getTokenCount(IZone zone) {
	    return tokens.get(zone);
	}

	protected void setTokenCount(IZone zone, Integer count) {
	    if (count <= 0) {
	        tokens.remove(zone);
	    } else {
	        tokens.put(zone, count);
	    }
	}

	protected void addTokens(IZone zone, Integer amount) {
	    Integer current = getTokenCount(zone);
	    setTokenCount(zone, current + amount);
	}

	protected Boolean removeTokens(IZone zone, Integer amount) {
	    Integer current = getTokenCount(zone);
	    if (current >= amount) {
	        setTokenCount(zone, current - amount);
	        return true;
	    }
	    return false;
	}

}
