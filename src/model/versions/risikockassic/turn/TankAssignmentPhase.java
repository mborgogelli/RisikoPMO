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
		return EnumPhaseRisikoNew.ASSIGNMENT.getPhaseId() + 1;
	}

	@Override
	public void endPhase() {
		// TODO Auto-generated method stub

	}

}
