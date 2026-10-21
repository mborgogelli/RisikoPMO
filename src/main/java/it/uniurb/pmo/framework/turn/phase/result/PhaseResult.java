package it.uniurb.pmo.framework.turn.phase.result;

import it.uniurb.pmo.framework.turn.phase.IPhasePayload;

import java.util.Optional;

public record PhaseResult<S extends IPhasePayload>(boolean isCompleted,
                                                   Optional<S> phasePayload) implements IPhaseResult<S> {
}
