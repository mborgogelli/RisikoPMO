package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;

public interface IMediatorRisikoNew extends IMediator {

    default List<String> getTerritoriesOwnedBy(IPlayer player){
        return getZonesOwnedBy(player);
    }

    default List<String> getAllTerritories(){
        return getAllZones();
    }

    default Map<String, Integer> acquireTargetZones(IPlayer player, int toDeploy){
        return acquireTargetZones(player, ERisikoNewToken.TANK, toDeploy);
    };

    int getPlayerTank(IPlayer player);

    int getZoneTank(String zone);

    void deployTank(IPlayer player, String zone, int tanks);


}
