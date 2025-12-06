package model.versions.risikockassic.turn;

public enum EnumPhaseRisikoNew {
	
	ASSIGNMENT(1),
	PLACEMENT(2),
	ATTACK(3),
	MOVEMENT(4);
	
	private final int phaseId;

    private EnumPhaseRisikoNew(int id) {
        this.phaseId = id;
    }
    
    public int getPhaseId() {
        return this.phaseId;
    }
}	
