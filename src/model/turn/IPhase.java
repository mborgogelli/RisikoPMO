package model.turn;

public interface IPhase {
	
	int getPhaseId();
	
	void playPhase();
	
	int nextPhase();
	
	void endPhase();
}
