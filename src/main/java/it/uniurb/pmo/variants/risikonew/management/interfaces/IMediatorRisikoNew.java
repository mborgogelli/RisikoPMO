package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCard;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.stream.Stream;

public interface IMediatorRisikoNew extends IMediator {

    List<String> getCompletedContinents(IPlayer player);

    int getPlayerTank(IPlayer player);

    void deployTank(IPlayer player, String zone, int tanks);

	Stream<List<ITerritoryCard>> getAvailableTris(IPlayer player);

    void playTris(IPlayer player, List<? extends ITerritoryCard> cards);

    default List<String> getTerritoriesOwnedBy(IPlayer player){
        return this.getZonesOwnedBy(player);
    }

    default List<String> getAllTerritories(){
        return getAllZones();
    }

    default int getZoneTank(String zone){
        return this.getZoneTokenByType(zone, ERisikoNewToken.TANK);
    };

    default int getContinentArmyBonus(String continent) {
        return getZoneValue(continent);
    };

    default int getTerritoryValue(String territory) {
        return getZoneValue(territory);
    };

    default void reinforcePlayer(IPlayer player, int reinforcements) {
        this.addTokens(player, ERisikoNewToken.TANK, reinforcements);
    };

    default void applyLosses(String zone, int losses) {
        this.removeTokensFromZone(zone, ERisikoNewToken.TANK, losses);
    };

    default void conquerTerritory(String territory, IPlayer newOwner) {
        this.changeZoneOwnership(territory,newOwner);
    };

    default void transferTanks(IPlayer player, String fromZone, String toZone, int tanks) {
        if(this.getZoneOwner(toZone) != player){
            this.changeZoneOwnership(toZone,player);
        }
        this.removeTokensFromZone(fromZone, ERisikoNewToken.TANK, tanks);
        this.addTokensToZone(toZone, ERisikoNewToken.TANK, tanks);
    };

}
