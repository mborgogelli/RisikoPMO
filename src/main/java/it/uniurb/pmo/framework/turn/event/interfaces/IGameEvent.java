package it.uniurb.pmo.framework.turn.event.interfaces;

import it.uniurb.pmo.framework.turn.dto.IGameSnapshot;
import it.uniurb.pmo.framework.turn.IPhaseType;
import it.uniurb.pmo.framework.turn.event.EGameEventType;

import java.util.Optional;

/**
 * Evento di gioco associato allo snapshot completo successivo alla transizione.
 */
public interface IGameEvent {

    EGameEventType getEventType();

    default Optional<IPhaseType> getPhaseType() {
        return Optional.empty();
    }

    /** Snapshot completo e aggiornato associato all'evento. */
    IGameSnapshot getSnapshot();

}
