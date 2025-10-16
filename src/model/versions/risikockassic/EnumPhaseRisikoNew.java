package model.versions.risikockassic;

import model.utils.EnumPhase;

public enum EnumPhaseRisikoNew implements EnumPhase{
	
	ASSIGNMENT(1),
	PLACEMENT(2),
	ATTACK(3),
	MOVEMENT(4);
	
	private final int phaseId;

    private EnumPhaseRisikoNew(int id) {
        this.phaseId = id;
    }
    
    @Override
    public int getPhaseId() {
        return this.phaseId;
    }
}	
