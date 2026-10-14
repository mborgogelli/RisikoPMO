package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;

import java.util.Optional;

public record PhaseResult<S extends IGameState>(boolean isCompleted,
                                                Optional<S> state) implements IPhaseResult<S> {
}
