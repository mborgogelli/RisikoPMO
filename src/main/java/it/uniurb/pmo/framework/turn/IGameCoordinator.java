package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.turn.command.IGameCommandPublisher;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventReceiver;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;

import java.util.Map;
import java.util.Optional;

/**
 * Classe per o scambio di informazioni tra il modello e i controller
 * Implementa il pattern Adapter per convertire le informazioni nella struttura
 * gestita dai controller.
 */
public interface IGameCoordinator extends IGameEventReceiver, IGameCommandPublisher {

    Optional<IGameEvent> lastGameEvent();

    Optional<IGameSnapshot> getLatestGameSnapshot();

    /**
     * Fornisce l'ultimo stato della partita nel DTO per il controller.
     */
    Optional<CurrentStateDTO> currentState(String roomId);

    /**
     * Invia al modello una richiesta di deploy.
     * Il coordinator traduce la richiesta nel comando concreto.
     */
    void submitDeploy(String playerName, String tokenType, Map<String, Integer> deployment);

    /**
     * Restituisce l'eventuale risultato di combattimento.
     * La variante deve specificare il risultato di combattimento.
     */
    default Optional<ICombatResultDTO> getCombatResult() {
        return Optional.empty();
    }

}
