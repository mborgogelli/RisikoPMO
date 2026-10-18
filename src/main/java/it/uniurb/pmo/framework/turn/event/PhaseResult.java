package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IPhasePayload;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;

import java.util.Optional;

public record PhaseResult<S extends IPhasePayload>(boolean isCompleted,
                                                Optional<S> state) implements IPhaseResult<S> {
}
