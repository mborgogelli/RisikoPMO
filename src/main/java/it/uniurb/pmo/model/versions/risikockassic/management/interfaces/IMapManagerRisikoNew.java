package it.uniurb.pmo.model.versions.risikockassic.management.interfaces;

import java.util.List;

import it.uniurb.pmo.model.management.interfaces.IMapManager;
import it.uniurb.pmo.model.players.IPlayer;

public interface IMapManagerRisikoNew extends IMapManager {
	
	default List<String> getTerritoriesOwnedBy(IPlayer player){
		return getZonesOwnedBy(player);
	}
	
	default List<String> getAllTerritories(){
        return getAllZones();
    }
}
