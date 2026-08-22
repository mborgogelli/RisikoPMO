package it.uniurb.pmo.framework.card;

import it.uniurb.pmo.framework.players.IPlayer;

/**
 * Interfaccia che rappresenta l'obiettivo di una missione.
 * Implementa un metodo per verificare se l'obiettivo è raggiungibile da un giocatore
 * Implementa un metodo per verificare se l'obiettivo è stato raggiunto da un giocatore.
 * 
 **/

public interface IMissionTarget extends ICardContent{

	 boolean isAchievementReachable(IPlayer player);
	
	 boolean isAchievementReached(IPlayer player);
}
