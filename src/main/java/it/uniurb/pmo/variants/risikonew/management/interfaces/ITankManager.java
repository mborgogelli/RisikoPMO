package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

public interface ITankManager extends ITokenManager {

	default int getPlayerTank(IPlayer player) {
		return getPlayerToken(player, ERisikoNewToken.TANK);
	}

	default int getPlayerTank(IPlayer player, ERisikoNewToken type) {
		return getPlayerToken(player, type);
	}

	default int getZoneTank(String zone) {
		return getZoneToken(zone);
	}
	
	default void deployTank(IPlayer player, String zone, int tank) {
		deployToken(player, ERisikoNewToken.TANK, zone, tank);
	}
	
	default void moveTank(IPlayer player, String toZone, String fromZone, int tanks) {
		moveToken(player, ERisikoNewToken.TANK, toZone, fromZone, tanks);
	}

	default void moveTank(IPlayer player, ERisikoNewToken type, String toZone, String fromZone, int tanks) {
		moveToken(player, type, toZone, fromZone, tanks);
	}
}
