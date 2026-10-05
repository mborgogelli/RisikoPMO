package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.IPhaseType;
import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

import java.util.Optional;

public record GameEvent<E extends IGameState>(EGameEventType type, IPhaseType phaseType, E state) implements IGameEvent<E> {

    @Override
    public EGameEventType getEventType() {
        return this.type;
    }

    @Override
    public Optional<IPhaseType> getPhaseType() {
        return Optional.ofNullable(this.phaseType);
    }

    @Override
    public E getState() {
        return this.state;
    }
}
