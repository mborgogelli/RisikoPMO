package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.IPlayer;

import java.util.List;
import java.util.stream.Stream;

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
	 *
	 * @param player il giocatore
	 * @param toZone la zona di destinazione
	 * @param fromZone la zona di partenza
	 * @return true se il giocatore può muovere il token, false altrimenti
	 */
	boolean canMoveBetween(IPlayer player, String toZone, String fromZone);



	void notifyWinner(IPlayer iPlayer);

	/**
	 * Verifica se un giocatore ha soddisfatto le condizioni di vittoria
	 */
	boolean checkVictory(IPlayer player);

	/**
	 * Restituisce le carte possedute da un giocatore.
	 * @param player il giocatore
	 * @param cardType il tipo di carta
	 */
	List<ICard> getPlayerCardsByType(IPlayer player, ICardType cardType);

	/**
	 *  Dice al cardManager di giocare una carta
	 * @param player il giocatore che gioca la carta
	 * @param card la carta da giocare
	 */
	void playCard(IPlayer player, ICard card);
	
	/**
	 * 
	 * @param playerCards
	 * @param k
	 * @return
	 */
	Stream<List<ICard>> getCombinationsOf(List<ICard> playerCards, int k);
	
}
