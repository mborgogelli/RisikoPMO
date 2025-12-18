package model.turn;

import model.players.IPlayer;

public interface IPhase {
	
	int getPhaseId();
	
	void playPhase(IPlayer player);
	
	int nextPhase();
	
	void endPhase();
}
