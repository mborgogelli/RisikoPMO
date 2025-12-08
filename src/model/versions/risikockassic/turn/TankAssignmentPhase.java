package model.versions.risikockassic.turn;

import model.turn.IPhase;

public class TankAssignmentPhase implements IPhase {

	@Override
	public int getPhaseId() {
		return EnumPhaseRisikoNew.ASSIGNMENT.getPhaseId();
	}

	@Override
	public void playPhase() {
		// TODO Auto-generated method stub

	}

	@Override
	public int nextPhase() {
		int currentPhaseId = getPhaseId();
		int phaseCount = EnumPhaseRisikoNew.values().length;
		return (currentPhaseId + 1) % phaseCount;
	}

	@Override
	public void endPhase() {
		// TODO Auto-generated method stub

	}

}
