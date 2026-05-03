package it.uniurb.pmo.framework.card;

import it.uniurb.pmo.framework.players.Player;

/**
 * Interfaccia che rappresenta l'obiettivo di una missione.
 * Implementa un metodo per verificare se l'obiettivo è stato raggiunto da un giocatore.
 */
public interface IMissionTarget {

	 boolean isAchievementReached(Player player);
}
