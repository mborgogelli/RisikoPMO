package it.uniurb.pmo.variants.risikonew.utils;

public enum ERisikoNewPhase {
	
	REINFORCE(1),
	ATTACK(2),
	MOVEMENT(3);
	
	private final int phaseId;

    ERisikoNewPhase(int id) {
        this.phaseId = id;
    }
    
    public int getId() {
        return this.phaseId;
    }

}
