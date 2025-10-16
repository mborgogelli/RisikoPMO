package model.management.interfaces;

import java.util.List;

import model.players.IPlayer;

public interface IManager {
	
	/**
	 * Verifica se il manager è pronto per l'uso
	 * @return true se il manager è pronto, false altrimenti
	 */
	Boolean isReady();
	
	/**
	 * Inizializza il gioco
	 */
	void initializeGame(List<IPlayer> players);
	
	/**
	 * Resetta il gioco al suo stato iniziale
	 */
	void resetGame();

}
