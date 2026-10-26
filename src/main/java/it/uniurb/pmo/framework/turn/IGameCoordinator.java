package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.turn.command.IGameCommandPublisher;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventReceiver;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;
import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;

import java.util.Optional;

/**
 * Canale di comunicazione tra la logica di gioco e i giocatori.
 * Il AbstractGameCoordinator si occupa di inviare messaggi/richieste al giocatore
 * e di raccogliere la risposta, mantenendo il framework agnostico rispetto
 * alla variante di gioco concreta.
 */
public interface IGameCoordinator extends IGameEventReceiver, IGameCommandPublisher {

    Optional<IGameEvent> lastGameEvent();

    Optional<IGameSnapshot> getLatestGameSnapshot();

    /**
     * Proietta l'ultimo stato della partita nel DTO esposto alla view.
     * La conversione comune è condivisa; i coordinator di variante aggiungono
     * solo gli eventuali dati specifici della propria variante.
     */
    Optional<CurrentStateDTO> currentState(String roomId);

}
