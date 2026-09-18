package it.uniurb.pmo.framework.turn.event;

/**
 * Tipi di cambiamento del flusso di gioco pubblicati dal TurnManager.
 */
public enum EGameEventType {
    TURN_STARTED,
    PHASE_STARTED,
    CHOICE_REQUIRED,
    GAME_ENDED
}
