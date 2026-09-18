package it.uniurb.pmo.framework.turn;

public record PhaseResult(boolean isCommandAccepted, boolean isPhaseCompleted, String errorMessage) implements IPhaseResult{
}
