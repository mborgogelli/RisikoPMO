package model.management.interfaces;

import model.utils.GameVersion;

public interface IRoomManager {
	
	/**
	 * Verifica se la versione del gioco è valida.
	 * 
	 * @param gameVersion la versione del gioco da verificare
	 * @return true se la versione è valida, false altrimenti
	 */
	boolean gameVersionIsValid(GameVersion gameVersion);
}
