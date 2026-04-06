package it.uniurb.pmo.model.utils;

import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;

public enum EnumPhase implements IPhase {
	
	ASSIGNMENT(1),
	REINFORCE(2),
	ATTACK(3),
	MOVEMENT(4);
	
	private final int phaseId;

    EnumPhase(int id) {
        this.phaseId = id;
    }
    
    @Override
    public int getId() {
        return this.phaseId;
    }

    @Override
    public void playPhase(IPlayer player) {

    }

}
