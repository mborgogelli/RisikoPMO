package it.uniurb.pmo.model.utils;

import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;

public enum EnumPhase implements IPhase {
	
	ASSIGNMENT(0),
	REINFORCE(1),
	ATTACK(2),
	MOVEMENT(3);
	
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

    @Override
    public void clearPhase() {

    }

}
