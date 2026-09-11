package it.uniurb.pmo.framework.turn;

/**
 * Aggiornamento immutabile del flusso di una partita.
 * Non espone oggetti del model ai suoi observer.
 *
 * @param type tipo di cambiamento avvenuto
 * @param playerName nome del giocatore coinvolto
 * @param turnNumber numero del turno corrente
 * @param phaseId identificativo della fase, nullo per eventi relativi al turno
 */
public record GameEvent(
        EGameEventType type,
        String playerName,
        int turnNumber,
        Integer phaseId) {
}
