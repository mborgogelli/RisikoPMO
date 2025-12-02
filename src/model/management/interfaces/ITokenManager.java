package model.management.interfaces;

import java.util.Map;

import model.players.IPlayer;

public interface ITokenManager extends IManager {
	
	Map<String, Integer> getDeployedPerZone(IPlayer player);
	
	int getTotalDeployed(IPlayer player);
	
	/**
	 * Ottiene il numero di tank di un giocatore
	 * 
	 * @param player il giocatore
	 * @return numero di tank posseduti
	 */
	int getPlayerToken(IPlayer player);

	/**
	 * Ottiene il numero di tank in una zona
	 * 
	 * @param zone la zona
	 * @return numero di tank nella zona
	 */
	int getZoneToken(String zone);

	/**
	 * Esegue il dispiegamento di tank in una zona per un giocatore.
	 * 
	 * @param player il giocatore che dispiega i tank
	 * @param zone la zona in cui i tank vengono dispiegati
	 * @param amount il numero di tank da dispiegare
	 * @throws IllegalStateException se il gioco non è pronto o il giocatore non possiede abbastanza tank
	 * @throws IllegalArgumentException se la zona non è di proprietà del giocatore
	 * 
	 */
	void deployToken(IPlayer player, String zone, int amount);

	void moveToken(IPlayer player, String toZone, String fromZone, int amount);
}
