package it.uniurb.pmo.model.management.interfaces;


import it.uniurb.pmo.model.players.IPlayer;

import java.util.Optional;

/**
 * Interfaccia che modella un mediatore tra i manager di gioco
 */
public interface IMediator {

	/**
	 * Registra un manager con il mediatore
	 */
	void registerManager(IManager manager);
	
	/**
	 * Inizializza tutti i manager registrati
	 */
	void initManagers();

	/**
	 * Inizia il gioco
	 */
	void startGame();

}
