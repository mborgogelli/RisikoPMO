package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.IPhaseType;
import it.uniurb.pmo.framework.turn.dto.IGameSnapshot;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

import java.util.Optional;

public record GameEvent(EGameEventType type, IPhaseType phaseType,
                        IGameSnapshot snapshot) implements IGameEvent {

    @Override
    public EGameEventType getEventType() {
        return this.type;
    }

    @Override
    public Optional<IPhaseType> getPhaseType() {
        return Optional.ofNullable(this.phaseType);
    }

    @Override
    public IGameSnapshot getSnapshot() {
        return this.snapshot;
    }
}
