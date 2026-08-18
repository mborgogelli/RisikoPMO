package it.uniurb.pmo.variants.risikonew.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.players.IPlayer;

public interface IMapManagerRisikoNew extends IMapManager {

	boolean isTerritory(String territory);

	boolean isContinent(String continent);

	default List<String> getTerritoriesOwnedBy(IPlayer player){
		return getZonesOwnedBy(player);
	}
	
	default List<String> getAllTerritories(){
        return getAllZones();
    }

	default int getTerritoryTimeAttackValue(String territory){
		int value = 0;
		if(isTerritory(territory)) {
			value = getZoneValue(territory);
		}
		return value;
	}

	default int getContinentCompletionArmyBonus(String continent){
		int value = 0;
		if(isContinent(continent)) {
			value = getZoneValue(continent);
		}
		return value;
	}
}
