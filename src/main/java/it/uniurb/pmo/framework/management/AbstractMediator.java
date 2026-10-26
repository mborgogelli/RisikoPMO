package it.uniurb.pmo.framework.management;


import it.uniurb.pmo.framework.management.interfaces.*;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe astratta che implementa
 * l'interfaccia IMediator e fornisce un'implementazione base per la gestione dei mediatori.
 *
 */
public abstract class AbstractMediator implements IMediator {
	
	private final List<IManager> managers;
	private IDirector director;

	public AbstractMediator() {
		this.managers = new ArrayList<>();
	}

	@Override
	public void setDirector(IDirector director) {
		this.director = director;
	}

	@Override
	public void  notifyWinner(IPlayer player) {
		this.director.declareWinner(player);
	}

    @Override
    public List<String> getAllZones() {
        return this.resolveManager(IMapManager.class).getAllZones();
    }

    @Override
    public List<String> getZonesOwnedBy(IPlayer player) {
        return this.resolveManager(IMapManager.class).getZonesOwnedBy(player);
    }

    @Override
    public boolean canMoveBetween(IPlayer player, String toZone, String fromZone) {
        return this.resolveManager(IMapManager.class).canMoveBetween(player, toZone, fromZone);
    }
    
	@Override
	public Map<ITokenType, Integer> getZoneTokens(String zone) {
		return this.resolveManager(ITokenManager.class).getZoneToken(zone);
	}

	@Override
	public IPlayer getZoneOwner(String zone) {
		return this.resolveManager(IMapManager.class).getOwner(zone);
	}

	@Override
	public Map<ITokenType,Integer> getTokensOwnedBy(IPlayer player) {
		return this.resolveManager(ITokenManager.class).getPlayerTokens(player);
	}

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



}
