package model.versions.risikockassic.turn.interfaces;

import java.util.List;

import model.card.ICard;
import model.players.IPlayer;
import model.turn.IPhase;
import model.versions.risikockassic.turn.EnumPhaseRisikoNew;

public interface IReinforcePhase extends IPhase {
	
	/* Calcola il numero di rinforzi basati sul numero di territori posseduti dal giocatore */
	int reinforceByTerritories(List<String> playerTerritories);
	
	/* Calcola il numero di rinforzi basati sui continenti posseduti dal giocatore */
	int reinforceByContinentBonus(String continent);
	
	/* Calcola il numero di rinforzi basati sulle carte scambiate dal giocatore */
	int reinforceByCards(IPlayer player, List<ICard> tris);
	
	/* Restituisce l'id della fase di rinforzo */
	default int getPhaseId() {
		return EnumPhaseRisikoNew.REINFORCE.getPhaseId();
	};
	
}
