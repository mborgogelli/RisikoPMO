package model.management;

import java.util.Set;

import model.management.interfaces.IManager;
import model.players.IPlayer;
import model.utils.EnumToken;

/**
 * TokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class TokenManager implements IManager{
	
	/**
	 * Restituisce i tipi di token gestiti da questo manager
	 * @return Set dei token gestiti
	 */
	protected abstract Set<EnumToken> getManagedTokens();
	
	
	protected abstract void resetTokenData();
	
	/**
	 * Restituisce il MapManager specifico per questa versione del gioco
	 * @return il MapManager da utilizzare per le validazioni territoriali
	 */
	protected abstract MapManager getMapManager();
		
	/**
	 * Verifica se un giocatore ha un numero sufficiente di token di un certo tipo.
	 * 
	 * @param player il giocatore da verificare
	 * @param tokenType il tipo di token da controllare
	 * @param required il numero minimo di token richiesti
	 * @return true se il giocatore ha almeno il numero richiesto di token, false altrimenti
	 */
	/*protected Boolean checkPlayerToken(IPlayer player, EnumToken tokenType, int required) {
		int available = player.getToken(tokenType);
		return available >= required;
	}*/
	
}