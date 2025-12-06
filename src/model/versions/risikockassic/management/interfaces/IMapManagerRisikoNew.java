package model.versions.risikockassic.management.interfaces;

import java.util.List;

import model.board.IZone;
import model.management.interfaces.IMapManager;
import model.players.IPlayer;

public interface IMapManagerRisikoNew extends IMapManager {
	
	public default List<String> getTerritoriesOwnedBy(IPlayer player){
		return getZonesOwnedBy(player);
	}
	
	public default List<String> getAllTerritories(){
        return getAllZones();
    }
}
