package it.uniurb.pmo.variants.risikonew.utils;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;

public enum EnumRisikoNewPhase implements IPhase {
	
	REINFORCE(1),
	ATTACK(2),
	MOVEMENT(3);
	
	private final int phaseId;

    EnumRisikoNewPhase(int id) {
        this.phaseId = id;
    }
    
    @Override
    public int getId() {
        return this.phaseId;
    }

    @Override
    public void playPhase(IPlayer player, IMediator mediator) {

    }

    @Override
    public void clearPhase() {

    }

}
