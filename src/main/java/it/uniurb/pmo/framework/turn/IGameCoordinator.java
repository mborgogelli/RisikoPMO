package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.players.ITokenType;
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

    /**
     *  Restituisce l'ultimo evento di gioco registrato.
     * @return  Evento di gioco.
     */
    Optional<IGameEvent> lastGameEvent();

    Optional<IGameSnapshot> getLatestGameSnapshot();

    /**
     * Fornisce l'ultimo stato della partita nel DTO per il controller.
     */
    Optional<CurrentStateDTO> currentState(String roomId);

    /**
     *  Risolve il tipo di token dato il suo nome.
     * @param tokenTypeName  Nome del tipo di token.
     * @return  Il tipo di token corrispondente al nome dato.
     */
    ITokenType resolveTokenType(String tokenTypeName);

    /**
     * Invia al modello una richiesta di deploy.
     * Il coordinator traduce la richiesta nel comando concreto.
     * @param playerName  Nome del giocatore che effettua il deploy.
     * @param tokenType  Nome del tipo di token da deployare.
     * @param deployment  Mappa che associa il nome della zona alla quantità di token da deployare.
     */
    void submitDeploy(String playerName, String tokenType, Map<String, Integer> deployment);

    /**
     *  Invia al modello una richiesta di attacco.
     * @param playerName  Nome del giocatore che effettua l'attacco.
     * @param attackerZone  Nome della zona attaccante.
     * @param defenderZone  Nome della zona difensiva.
     * @param tokenType  Tipo di token utilizzato nell'attacco.
     * @param tokenAmount  Numero di token utilizzati nell'attacco.
     */
    void submitAttack(String playerName, String attackerZone, String defenderZone,
                      ITokenType tokenType, int tokenAmount);

    /**
     *  Invia al modello una richiesta di difesa.
     * @param playerName  Nome del giocatore che effettua la difesa.
     * @param tokenType  Tipo di token utilizzato nella difesa.
     * @param tokenAmount  Numero di token utilizzati nella difesa.
     */
    void submitDefend(String playerName, ITokenType tokenType, int tokenAmount);

    /**
     *  Invia al modello una richiesta di passaggio.
     * @param playerName  Nome del giocatore che effettua il passaggio.
     */
    void submitPass(String playerName);

    void submitMovement(String playerName, ITokenType tokenType, String source,
                        String destination, int tokenAmount);

    /**
     * Restituisce l'eventuale risultato di combattimento.
     * La variante deve specificare il risultato di combattimento.
     * @return  Eventuale risultato di combattimento.
     */
    default Optional<ICombatResultDTO> getCombatResult() {
        return Optional.empty();
    }

}
