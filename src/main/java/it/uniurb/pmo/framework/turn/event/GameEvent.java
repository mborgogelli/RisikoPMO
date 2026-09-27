package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;

public record GameEvent<E extends IGameState>(EGameEventType type, E state) implements IGameEvent<E> {

    @Override
    public EGameEventType getEventType() {
        return this.type;
    }

    @Override
    public E getState() {
        return this.state;
    }
}
