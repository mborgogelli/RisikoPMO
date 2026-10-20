package it.uniurb.pmo.framework.turn.event.interfaces;

import it.uniurb.pmo.framework.turn.dto.IGameSnapshot;
import it.uniurb.pmo.framework.turn.event.EGameEventType;

/**
 * Evento di gioco associato allo snapshot completo successivo alla transizione.
 */
public interface IGameEvent {

    EGameEventType getEventType();

    /** Snapshot completo e aggiornato associato all'evento. */
    IGameSnapshot getSnapshot();

}
