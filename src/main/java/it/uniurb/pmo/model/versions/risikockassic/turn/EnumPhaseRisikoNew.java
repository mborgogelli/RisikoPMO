package it.uniurb.pmo.model.versions.risikockassic.turn;

public enum EnumPhaseRisikoNew {
	
	INITIALSETUP(0),
	REINFORCE(1),
	ATTACK(2),
	MOVEMENT(3);
	
	private final int phaseId;

    private EnumPhaseRisikoNew(int id) {
        this.phaseId = id;
    }
    
    public int getPhaseId() {
        return this.phaseId;
    }
}	
