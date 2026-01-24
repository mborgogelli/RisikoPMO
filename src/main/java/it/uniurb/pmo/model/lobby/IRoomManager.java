package it.uniurb.pmo.model.lobby;

import it.uniurb.pmo.model.utils.GameVersion;

public interface IRoomManager {
	
	/**
	 * Verifica se la versione del gioco è valida.
	 * 
	 * @param gameVersion la versione del gioco da verificare
	 * @return true se la versione è valida, false altrimenti
	 */
	boolean gameVersionIsValid(GameVersion gameVersion);
	
	/**
	 * Ottiene l'ID della stanza a cui appartiene un giocatore.
	 * 
	 * @param playerName il nome del giocatore
	 * @return l'ID della stanza del giocatore
	 */
	String getRoomIdByPlayerName(String playerName);
	
	/**
	 * Permette a un giocatore di entrare in una stanza.
	 * 
	 * @param nomeGiocatore il nome del giocatore che entra nella stanza
	 */
	void enterRoom(String roomId, String nomeGiocatore);
	
	/**
	 * Crea una nuova stanza di gioco.
	 * 
	 * @param nomeGiocatore il nome del giocatore che crea la stanza
	 * @param maxPlayers il numero massimo di giocatori nella stanza
	 * @param gemeVersion la versione del gioco per la stanza
	 * @return la stanza appena creata
	 */
	Room createRoom(String nomeGiocatore, int maxPlayers, GameVersion gemeVersion);
	
	/**
	 * Permette a un giocatore di uscire da una stanza.
	 * 
	 * @param roomId l'ID della stanza da cui il giocatore esce
	 * @param nomeGiocatore il nome del giocatore che esce dalla stanza
	 */
	void exitRoom(String roomId, String nomeGiocatore);
	
	/**
	 * Ritorna la stanza corrispondente all'ID fornito.
	 * 
	 * @param roomId l'ID della stanza da recuperare
	 * @return la stanza corrispondente all'ID
	 */
	Room getRoom(String roomId);
}
