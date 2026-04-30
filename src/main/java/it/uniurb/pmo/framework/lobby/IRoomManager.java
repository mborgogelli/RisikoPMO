package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.framework.utils.GameVersion;

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
	String createRoom(String nomeGiocatore, int maxPlayers, GameVersion gemeVersion);

	List<String> filterRoomsByGameVersion(GameVersion gameVersion);
	
	int getPlayersNumber(String roomId);

	EnumColors getPlayerColor(String roomId, String playerName);

	GameVersion getGameVersion(String roomId);

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

	int getMaxPlayers(String roomId);

	List<IPlayer> getPlayers(String roomId);

	Boolean isFull(String roomId);

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
