package model.versions.risikockassic;

import java.util.List;

import model.board.IZone;
import model.management.interfaces.IMapManager;
import model.players.IPlayer;

public interface IMapManagerRisikoNew extends IMapManager {
	
	public default List<IZone> getTerritoriesOwnedBy(IPlayer player){
		return getZonesOwnedBy(player);
	}
	
	public default List<IZone> getAllTerritories(){
        return getAllZones();
    }
}
