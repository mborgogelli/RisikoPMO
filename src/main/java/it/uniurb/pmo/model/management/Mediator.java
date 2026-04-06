
package it.uniurb.pmo.model.management;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.players.IPlayer;

public abstract class Mediator implements IMediator {
	
	private List<IManager> managers;
	
	public Mediator() {
		this.managers = new ArrayList<>();
	}
	
	// Metodi per accedere ai dati del MapManager
    public abstract List<String> getAllZones();
    public abstract List<String> getZonesOwnedBy(IPlayer player);
    public abstract Map<String,Integer> getZoneCount();
    public abstract boolean canMoveBetween(IPlayer player, String toZone, String fromZone);
    
    @Override
    public void registerManager(IManager manager) {
        this.managers.add(manager);
    }
    
	protected <T extends IManager> T getManager(Class<T> managerType) {
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
