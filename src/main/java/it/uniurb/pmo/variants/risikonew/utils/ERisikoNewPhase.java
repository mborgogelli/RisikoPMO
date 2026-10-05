package it.uniurb.pmo.variants.risikonew.utils;

import it.uniurb.pmo.framework.turn.IPhaseType;

public enum ERisikoNewPhase implements IPhaseType {
	
	INITIAL_PLACEMENT(0, "RISIKONEW_INITIAL_PLACEMENT"),
	REINFORCE(1, "RISIKONEW_REINFORCE"),
	ATTACK(2, "RISIKONEW_ATTACK"),
	MOVEMENT(3, "RISIKONEW_MOVEMENT"),
	STRATEGIC(4, "RISIKONEW_STRATEGIC");
	
	private final int phaseId;
	private final String code;

    ERisikoNewPhase(int id, String code) {
        this.phaseId = id;
		this.code = code;
    }
    
    public int getId() {
        return this.phaseId;
    }

	@Override
	public String phaseCode() {
		return this.code;
	}

}
