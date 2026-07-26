package it.uniurb.pmo.variants.risikonew.turn.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewPhase;

public interface IReinforcePhase extends IPhase {
	
	/* Calcola il numero di rinforzi basati sul numero di territori posseduti dal giocatore */
	int reinforceByTerritories(List<String> playerTerritories);
	
	/* Calcola il numero di rinforzi basati sui continenti posseduti dal giocatore */
	int reinforceByContinentBonus(String continent);
	
	/* Calcola il numero di rinforzi basati sulle carte scambiate dal giocatore */
	int reinforceByCards(List<ICard> tris);
	
	/* Restituisce l'id della fase di rinforzo */
	default int getId() {
		return ERisikoNewPhase.REINFORCE.getId();
	};

}
