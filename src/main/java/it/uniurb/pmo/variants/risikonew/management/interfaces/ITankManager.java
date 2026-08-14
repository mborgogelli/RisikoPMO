package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import java.util.Map;

/**
 * Interfaccia che definisce le operazioni per la gestione dei tank.
 */
public interface ITankManager extends ITokenManager {

	/**
	 *  Permette di ottenere il tipo di token di default per i tank.
	 * @return il tipo di token di default per i tank
	 */
	ITokenType getDefaultTokenType();

	default int getTotalDeployed(IPlayer player) {
		return getTotalDeployed(player, getDefaultTokenType());
	}

	default int getPlayerTank(IPlayer player) {
		return getPlayerToken(player, getDefaultTokenType());
	}

	default int getTerritoryTanks(String zone) {
		Map<ITokenType, Integer> tokens = getZoneToken(zone);
		return tokens.getOrDefault(getDefaultTokenType(), 0);
	}

	default int getZoneTank(String zone) {
		return getTerritoryTanks(zone);
	}
	
	default void deployTank(IPlayer player, String zone, int tank) {
		deployToken(player, getDefaultTokenType(), zone, tank);
	}
	
	default void moveTank(IPlayer player, String toZone, String fromZone, int tanks) {
		moveToken(player, getDefaultTokenType(), toZone, fromZone, tanks);
	}

	default void assignTank(IPlayer player, int tanks) {
		assignToken(player, getDefaultTokenType(), tanks);
	}

	default void removeTank(IPlayer player, int tanks) {
		removeToken(player, getDefaultTokenType(), tanks);
	}
}
