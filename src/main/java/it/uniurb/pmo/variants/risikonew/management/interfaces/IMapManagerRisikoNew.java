package it.uniurb.pmo.variants.risikonew.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.players.IPlayer;

public interface IMapManagerRisikoNew extends IMapManager {
	
	default List<String> getTerritoriesOwnedBy(IPlayer player){
		return getZonesOwnedBy(player);
	}
	
	default List<String> getAllTerritories(){
        return getAllZones();
    }
}
