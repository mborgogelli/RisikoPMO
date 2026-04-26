package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;

public interface ITankManager extends ITokenManager {
	
	default int getPlayerTank(IPlayer player) {
		return getPlayerToken(player);
	}
	
	default int getZoneTank(String zone) {
		return getZoneToken(zone);
	}
	
	default void deployTank(IPlayer player, String zone, int tank) {
		deployToken(player, zone, tank);
	}
	
	default void moveTank(IPlayer player, String toZone, String fromZone, int tanks) {
		moveToken(player, toZone, fromZone, tanks);
	}
}
