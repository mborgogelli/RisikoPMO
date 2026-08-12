package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import java.util.Map;

public interface ITankManager extends ITokenManager {

	default int getTotalDeployed(IPlayer player) {
		return getTotalDeployed(player, ERisikoNewToken.TANK);
	}

	default int getPlayerTank(IPlayer player) {
		return getPlayerToken(player, ERisikoNewToken.TANK);
	}

	default int getPlayerToken(IPlayer player) {
		return getPlayerTank(player);
	}

	default int getTerritoryTanks(String zone) {
		Map<ITokenType, Integer> tokens = getZoneToken(zone);
		return tokens.getOrDefault(ERisikoNewToken.TANK, 0);
	}

	default int getZoneTank(String zone) {
		return getTerritoryTanks(zone);
	}
	
	default void deployTank(IPlayer player, String zone, int tank) {
		deployToken(player, ERisikoNewToken.TANK, zone, tank);
	}
	
	default void moveTank(IPlayer player, String toZone, String fromZone, int tanks) {
		moveToken(player, ERisikoNewToken.TANK, toZone, fromZone, tanks);
	}

}
