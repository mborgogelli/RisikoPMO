package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

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
