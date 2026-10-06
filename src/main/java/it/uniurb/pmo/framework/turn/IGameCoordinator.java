package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.turn.command.IGameCommandPublisher;
import it.uniurb.pmo.framework.turn.dto.GameSnapshotDTO;
import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventReceiver;

import java.util.Optional;

/**
 * Canale di comunicazione tra la logica di gioco e i giocatori.
 * Il AbstractGameCoordinator si occupa di inviare messaggi/richieste al giocatore
 * e di raccogliere la risposta, mantenendo il framework agnostico rispetto
 * alla variante di gioco concreta.
 */
public interface IGameCoordinator extends IGameEventReceiver, IGameCommandPublisher {

    Optional<IGameEvent<? extends IGameState>> lastGameEvent();

    Optional<GameSnapshotDTO> getLatestGameSnapshot();

}
