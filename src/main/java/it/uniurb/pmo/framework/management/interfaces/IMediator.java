package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;

import java.util.List;
import java.util.Map;
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

	void notifyWinner(IPlayer iPlayer);

	/**
	 * Verifica se un giocatore ha soddisfatto le condizioni di vittoria
	 */
	boolean checkVictory(IPlayer player);

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

	/**
	 * Restituisce le zone confinanti di una zona.
	 * @param zone la zona
	 * @return le zone confinanti
	 */
	List<String> getNeighboursOf(String zone);

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
	 * Restituisce tutte le combinazioni di k carte possedute da un giocatore.
	 * @param playerCards le carte possedute dal giocatore
	 * @param k il numero di carte da combinare
	 * @return uno stream (flusso) di liste di carte
	 */
	<T extends ICard> Stream<List<T>> getCombinationsOf(List<T> playerCards, int k);

	/**
	 * Restituisce il numero di token di un certo tipo in una zona.
	 * @param zone la zona
	 * @param tokenType il tipo di token
	 * @return il numero di token di quel tipo nella zona
	 */
	int getZoneTokenByType(String zone, ITokenType tokenType);

	/**
	 * Restituisce la mappa dei token presenti in una zona.
	 * @param zone la zona
	 * @return una mappa che associa ogni tipo di token al numero di token presenti nella zona
	 */
	Map<ITokenType, Integer> getZoneTokens(String zone);

	/**
	 * Restituisce la mappa dei token posseduti da un giocatore.
	 * @param player il giocatore
	 * @return una mappa che associa ogni tipo di token al numero di token posseduti dal giocatore
	 */
	Map<ITokenType,Integer> getTokensOwnedBy(IPlayer player);

	/**
	 * Metodo di rinforzo per un giocatore con un certo numero di token di un certo tipo.
	 * @param player il giocatore da rinforzare
	 * @param tokenType il tipo di token
	 * @param reinforcements il numero di token da aggiungere
	 */
	void reinforcePlayer(IPlayer player, ITokenType tokenType, int reinforcements);
}
