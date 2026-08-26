package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.stream.Stream;

public interface IMediatorRisikoNew extends IMediator {

    default List<String> getTerritoriesOwnedBy(IPlayer player){
        return getZonesOwnedBy(player);
    }

    default List<String> getAllTerritories(){
        return getAllZones();
    }

    List<String> getCompletedContinents(IPlayer player);

    int getPlayerTank(IPlayer player);

    int getZoneTank(String zone);

    void deployTank(IPlayer player, String zone, int tanks);

    default int getContinentArmyBonus(String continent) {
        return getZoneValue(continent);
    };

    default int getTerritoryValue(String territory) {
        return getZoneValue(territory);
    };
    
	Stream<List<ICard>> getAvailableTris(IPlayer player);

    void playTris(IPlayer player, List<ICard> cards);

    default void reinforcePlayer(IPlayer player, int reinforcements) {
        this.reinforcePlayer(player, ERisikoNewToken.TANK, reinforcements);
    };
}
