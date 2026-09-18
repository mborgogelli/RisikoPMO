package it.uniurb.pmo.framework.turn;

public interface IPhaseResult {

    boolean isCommandAccepted();

    boolean isPhaseCompleted();

    String errorMessage();
}
