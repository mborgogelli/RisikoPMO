package it.uniurb.pmo.framework.management;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniurb.pmo.framework.management.interfaces.IManager;
import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;

public abstract class AbstractMediator implements IMediator {
	
	private final List<IManager> managers;
	private final Map<IPlayer, PlayerTurnStatus> playerStatus;

	public AbstractMediator() {
		this.managers = new ArrayList<>();
		this.playerStatus = new HashMap<>();
	}
	
	// Metodi per accedere ai dati del MapManager
    public abstract List<String> getAllZones();
    public abstract List<String> getZonesOwnedBy(IPlayer player);
    public abstract boolean canMoveBetween(IPlayer player, String toZone, String fromZone);
    
    @Override
    public void registerManager(IManager manager) {
        this.managers.add(manager);
    }
    
      protected <T extends IManager> T resolveManager(Class<T> managerType) {
        T myManager = null;
		for (IManager manager : this.managers) {
            if (managerType.isInstance(manager)) {
            	myManager = managerType.cast(manager);
            }
        }
		if (myManager == null) {
	     	throw new IllegalArgumentException("Manager of type " + managerType.getName() + " not found.");
	    }
        return myManager;
    }

	@Override
	public void initializePlayerStatus(List<IPlayer> players) {
		this.playerStatus.clear();
		for (IPlayer player : players) {
			this.playerStatus.put(player, PlayerTurnStatus.ACTIVE);
		}
	}

	@Override
	public void setPlayerStatus(IPlayer player, PlayerTurnStatus status) {
		this.playerStatus.put(player, status);
	}

	@Override
	public PlayerTurnStatus getPlayerStatus(IPlayer player) {
		return this.playerStatus.getOrDefault(player, PlayerTurnStatus.ACTIVE);
	}

	@Override
	public boolean isPlayerActive(IPlayer player) {
		return this.getPlayerStatus(player) == PlayerTurnStatus.ACTIVE;
	}

	public void notifyWinner(IPlayer iPlayer) {
		//To do implement notification properly
	}

	@Override
	public boolean checkVictory(IPlayer player) {
		return false;   // To do delegate to MissionManager or CardManager
	}

}
