package it.uniurb.pmo.model.management.interfaces;

import java.util.Map;

import it.uniurb.pmo.model.players.IPlayer;

public interface ITokenManager extends IManager {
	
	/**
	 * Ottiene il numero di token dispiegati in ogni zona per un giocatore
	 * 
	 * @param player il giocatore
	 * @return mappa con le zone come chiavi e il numero di token dispiegati come
	 *         valori
	 */
	Map<String, Integer> getDeployedPerZone(IPlayer player);
	
	/**
	 * Ottiene il numero totale di token dispiegati da un giocatore
	 * 
	 * @param player il giocatore
	 * @return numero totale di token dispiegati
	 */
	int getTotalDeployed(IPlayer player);
	
	/**
	 * Ottiene il numero di token di un giocatore
	 * 
	 * @param player il giocatore
	 * @return numero di token posseduti
	 */
	int getPlayerToken(IPlayer player);

	/**
	 * Ottiene il numero di token in una zona
	 * 
	 * @param zone la zona
	 * @return numero di token nella zona
	 */
	int getZoneToken(String zone);

	/**
	 * Esegue il dispiegamento di token in una zona per un giocatore.
	 * 
	 * @param player il giocatore che dispiega i token
	 * @param zone la zona in cui i token vengono dispiegati
	 * @param amount il numero di token da dispiegare
	 * 
	 */
	void deployToken(IPlayer player, String zone, int amount);

	/**
	 * Muove i token di un giocatore da una zona ad un'altra.
	 * 
	 * @param player   il giocatore che muove i token
	 * @param toZone   la zona di destinazione
	 * @param fromZone la zona di partenza
	 * @param amount   il numero di token da muovere
	 */
	void moveToken(IPlayer player, String toZone, String fromZone, int amount);
	
	/**
	 * Aggiunge token al giocatore
	 * @param player
	 * @param token
	 */
	void assignToken(IPlayer player, int token);
	
}
