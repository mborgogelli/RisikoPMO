package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;

/**
 * Interfaccia che modella un mediatore tra i manager di gioco
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
	 * Verifica se il giocatore puo' muovere un token tra due zone.
	 */
	boolean canMoveBetween(IPlayer player, String toZone, String fromZone);


	void notifyWinner(IPlayer iPlayer);

	/**
	 * Verifica se un giocatore ha soddisfatto le condizioni di vittoria
	 */
	boolean checkVictory(IPlayer player);

	/**
	 * Inizializza lo stato dei giocatori per la turnazione.
	 */
	void initializePlayerStatus(List<IPlayer> players);

	/**
	 * Aggiorna lo stato di un giocatore.
	 */
	void setPlayerStatus(IPlayer player, PlayerTurnStatus status);

	/**
	 * Recupera lo stato di un giocatore.
	 */
	PlayerTurnStatus getPlayerStatus(IPlayer player);

	/**
	 * Verifica se il giocatore puo' prendere un turno.
	 */
	boolean isPlayerActive(IPlayer player);
}
