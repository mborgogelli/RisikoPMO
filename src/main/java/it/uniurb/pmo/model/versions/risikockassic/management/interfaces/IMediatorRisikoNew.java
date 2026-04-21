package it.uniurb.pmo.model.versions.risikockassic.management.interfaces;

import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.players.IPlayer;

import java.util.List;

public interface IMediatorRisikoNew extends IMediator {

    default List<String> getTerritoriesOwnedBy(IPlayer player){
        return getZonesOwnedBy(player);
    }

    default List<String> getAllTerritories(){
        return getAllZones();
    }
}
