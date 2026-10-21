package it.uniurb.pmo.framework.turn.event.interfaces;

import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;

/**
 * Evento di gioco associato allo snapshot completo successivo alla transizione.
 */
public interface IGameEvent {

    //TODO incapsulare EGameEventType in un'interfaccia
    EGameEventType getEventType();

    /** Snapshot completo e aggiornato associato all'evento. */
    IGameSnapshot getSnapshot();

}
