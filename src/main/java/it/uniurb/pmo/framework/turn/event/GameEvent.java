package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IGameSnapshot;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

public record GameEvent(EGameEventType type, IGameSnapshot snapshot) implements IGameEvent {

    @Override
    public EGameEventType getEventType() {
        return this.type;
    }

    @Override
    public IGameSnapshot getSnapshot() {
        return this.snapshot;
    }
}
