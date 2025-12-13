package model.versions.risikockassic.turn.interfaces;

import java.util.List;

import model.card.ICard;
import model.players.IPlayer;
import model.turn.IPhase;
import model.versions.risikockassic.turn.EnumPhaseRisikoNew;

public interface IReinforcePhase extends IPhase {
	
	int reinforceByTerritories(List<String> playerTerritories);
	
	int reinforceByContinentBonus(String continent);
	
	int reinforceByCards(IPlayer player, List<ICard> tris);
	
	default int getPhaseId() {
		return EnumPhaseRisikoNew.REINFORCE.getPhaseId();
	};
	
}
