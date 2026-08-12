package it.uniurb.pmo.framework.management.interfaces;

import java.util.Map;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;

public interface ITokenManager extends IManager {
	
	/**
	 * Ottiene il numero di token di un giocatore
	 * 
	 * @param player il giocatore
	 * @return numero di token posseduti
	 */
	int getPlayerToken(IPlayer player, ITokenType type);

	/**
	 * Ottiene il numero di token in una zona
	 * 
	 * @param zone la zona
	 * @return i token nella zona
	 */
	Map<ITokenType, Integer> getZoneToken(String zone);

	/**
	 * Esegue il dispiegamento di token in una zona per un giocatore.
	 * 
	 * @param player il giocatore che dispiega i token
	 * @param type il tipo di token da dispiegare
	 * @param amount il numero di token da dispiegare
	 * 
	 */
	void deployToken(IPlayer player, ITokenType type, String zone, int amount);

	/**
	 * Muove i token di un giocatore da una zona ad un'altra.
	 * 
	 * @param player   il giocatore che muove i token
	 * @param type     il tipo di token da muovere
	 * @param toZone   la zona di destinazione
	 * @param fromZone la zona di partenza
	 * @param amount   il numero di token da muovere
	 */
	void moveToken(IPlayer player, ITokenType type, String toZone, String fromZone, int amount);

	/**
	 * Aggiunge token al giocatore
	 * @param player
	 * @param type
	 * @param token
	 */
	void assignToken(IPlayer player, ITokenType type, int token);

	void assignToken(IPlayer player, int token);

	void removeToken(IPlayer player, ITokenType type, int token);

	void removeToken(IPlayer player, int token);
}
