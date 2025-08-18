package model.director;

import model.utils.GameVersion;

public class IDirector {
	
	/**
	 * Verifica se la versione del gioco è valida.
	 * 
	 * @param gameVersion la versione del gioco da verificare
	 * @return true se la versione è valida, false altrimenti
	 */
	private boolean gameVersionIsValid(GameVersion gameVersion) {
		Boolean isValid = false;
		for (GameVersion version : GameVersion.values()) {
			if (version == gameVersion) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
}
