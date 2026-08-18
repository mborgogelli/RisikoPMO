package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.players.IPlayer;

/**
 * Interfaccia che modella un mediatore tra i manager di gioco
 * Lo scopo del mediatore è quello di gestire la comunicazione tra i vari manager di gioco.
 *
 */
public interface IMediator extends IGameConductor {

	/**
	 * Registra un manager con il mediatore
	 */
	void registerManager(IManager manager);
	
	/**
	 * Inizializza tutti i manager registrati
	 */
	void initManagers();

	/**
	 * Restituisce tutte le zone di gioco.
	 */
	List<String> getAllZones();

	/**
	 * Restituisce le zone possedute da un giocatore.
	 */
	List<String> getZonesOwnedBy(IPlayer player);

	/**
	 * Restituisce il valore numerico associato a una zona.
	 * @param zone la zona
	 * @return il valore della zona
	 */
	int getZoneValue(String zone);

	/**
	 * Verifica se il giocatore puo' muovere un token tra due zone.
	 */
	boolean canMoveBetween(IPlayer player, String toZone, String fromZone);



	void notifyWinner(IPlayer iPlayer);

	/**
	 * Verifica se un giocatore ha soddisfatto le condizioni di vittoria
	 */
	boolean checkVictory(IPlayer player);


}
