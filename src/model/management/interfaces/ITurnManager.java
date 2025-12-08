package model.management.interfaces;

public interface ITurnManager {
	
	/**
	 * Comincia il turno del giocatore.
	 */
	void startTurn();
	
	/**
	 * Termina il turno del giocatore corrente.
	 */
	void endTurn();
	
	/**
	 * Ritorna la fase del turno.
	 */
	int getCurrentPhase();
	
	
}
