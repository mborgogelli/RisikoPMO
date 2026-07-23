package it.uniurb.pmo.framework.players;

import it.uniurb.pmo.framework.utils.Pair;

import java.util.Map;

/**
 * Canale di acquisizione delle scelte del giocatore.
 * Disaccoppia la logica di gioco dalla sorgente dell'input
 *
 * Non contiene logica di gioco: le validazioni restano nei manager.
 */
public interface IPlayerInputProvider {

    /**
     * Chiede al giocatore come vuole distribuire i rinforzi tra i territori.
     *
     * @param player      il giocatore che deve dispiegare
     * @param tanksToDeploy numero totale di carri armati da piazzare
     * @return mappa territorio → numero di carri armati da piazzare
     */
    Map<String, Integer> acquireDeployment(IPlayer player, int tanksToDeploy);

    /**
     * Chiede al giocatore di scegliere la zona da cui attaccare e quella da attaccare.
     *
     * @param player il giocatore attaccante
     * @return coppia (zona Bersaglio, zona Attaccante);
     */
    Pair<String,String> acquireAttack(IPlayer player);

    /**
     * Chiede al giocatore quanti carri armati spostare dopo una conquista.
     *
     * @param player    il giocatore che ha conquistato
     * @param fromZone  zona di partenza
     * @param toZone    zona appena conquistata
     * @param max       massimo numero di carri spostabili
     * @return numero di carri da spostare (compreso tra 1 e max)
     */
    int acquireTroopMovement(IPlayer player, String toZone, String fromZone, int max);

    /**
     * Chiede al giocatore di scegliere zona destinazione e zona sorgente per il
     * movimento strategico (fase finale del turno).
     *
     * @param player il giocatore che effettua il movimento
     * @return coppia (zona Destinazione, zona Origine); null se il giocatore rinuncia
     */
    Pair<String,String> acquireStrategicMovement(IPlayer player);
}
