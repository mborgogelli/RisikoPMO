package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.framework.utils.EGameVersion;

import java.util.List;

public interface IRoomManager {

	/**
	 * Crea una nuova stanza di gioco.
	 *
	 * @param nomeGiocatore il nome del giocatore che crea la stanza
	 * @param maxPlayers il numero massimo di giocatori nella stanza
	 * @param gemeVersion la versione del gioco per la stanza
	 * @return l'Id della stanza appena creata
	 */
	String createRoom(String nomeGiocatore, int maxPlayers, EGameVersion gemeVersion);

	/**
	 * Filtra le stanze in base alla versione del gioco.
	 *
	 * @param gameVersion la versione del gioco da filtrare
	 * @return una lista di ID delle stanze che corrispondono alla versione del gioco
	 */
	List<String> filterRoomsByGameVersion(EGameVersion gameVersion);
	
	/**
	 * Restituisce il numero di giocatori presenti in una stanza.
	 *
	 * @param roomId l'ID della stanza
	 * @return il numero di giocatori nella stanza
	 */
	int getPlayersNumber(String roomId);

	/**
	 * Restituisce il colore assegnato a un giocatore in una stanza.
	 *
	 * @param roomId l'ID della stanza
	 * @param playerName il nome del giocatore
	 * @return il colore assegnato al giocatore
	 */
	EColors getPlayerColor(String roomId, String playerName);

	/**
	 * Restituisce la versione del gioco associata a una stanza.
	 *
	 * @param roomId l'ID della stanza
	 * @return la versione del gioco della stanza
	 */
	EGameVersion getGameVersion(String roomId);

	/**
	 * Permette a un giocatore di entrare in una stanza.
	 * 
	 * @param nomeGiocatore il nome del giocatore che entra nella stanza
	 */
	void enterRoom(String roomId, String nomeGiocatore);
	

	/**
	 * Permette a un giocatore di uscire da una stanza.
	 * 
	 * @param roomId l'ID della stanza da cui il giocatore esce
	 * @param nomeGiocatore il nome del giocatore che esce dalla stanza
	 */
	void exitRoom(String roomId, String nomeGiocatore);

	/**
	 * Restituisce il numero massimo di giocatori consentiti in una stanza.
	 *
	 * @param roomId l'ID della stanza
	 * @return il numero massimo di giocatori nella stanza
	 */
	int getMaxPlayers(String roomId);

	/**
	 * Restituisce la lista dei giocatori presenti in una stanza.
	 *
	 * @param roomId l'ID della stanza
	 * @return la lista dei giocatori nella stanza
	 */
	List<IPlayer> getPlayers(String roomId);

	/**
	 * Verifica se una stanza è piena.
	 *
	 * @param roomId l'ID della stanza
	 * @return true se la stanza è piena, false altrimenti
	 */
	Boolean isFull(String roomId);

	/**
	 * Chiude una stanza di gioco.
	 *
	 * @param roomId l'ID della stanza da chiudere
	 */
	void closeRoom(String roomId);

	/**
	 * Restituisce una lista degli ID di tutte le stanze attive.
	 */
	List<String> getActiveRooms();

	/**
	 * Imposta lo stato di pronto di un giocatore.
	 */
	void setPlayerReady(String roomId, String playerName, boolean isReady);

	/**
	 * Verifica se tutti i giocatori in stanza sono pronti.
	 */
	boolean areAllPlayersReady(String roomId);
}
