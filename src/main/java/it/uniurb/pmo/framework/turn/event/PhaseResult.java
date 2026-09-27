package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;

import java.util.Optional;

public record PhaseResult(boolean isCompleted,
                          Optional<? extends IGameState> choiceRequired) implements IPhaseResult {
}
