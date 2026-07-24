package it.uniurb.pmo.framework.players;

import java.util.Map;
import java.util.List;

/**
 * Canale bidirezionale tra la logica di gioco e il giocatore.
 *
 * Espone metodi sincroni di acquisizione input: il chiamante fornisce il contesto
 * necessario e riceve immediatamente la scelta del giocatore.
 */
public interface IPlayerInputProvider {

    /**
     * Chiede al giocatore come vuole distribuire le pedine tra i territori.
     *
     * @param player          giocatore che deve dispiegare
     * @param deployableZones zone in cui il giocatore può dispiegare
     * @param availableTokens mappa tipoPedina -> quantità disponibile da piazzare
     * @return mappa territorio → (tipoPedina → quantità piazzata in quel territorio)
     */
    Map<String, Map<ITokenType, Integer>> acquireDeployment(
            IPlayer player,
            List<String> deployableZones,
            Map<ITokenType, Integer> availableTokens
    );

    /**
     * Chiede al giocatore di scegliere la zona da cui attaccare e quella da attaccare.
     *
     * @param player il giocatore attaccante
     * @return coppia [zonaAttaccante, zonaBersaglio]; null se il giocatore rinuncia ad attaccare
     */
    String[] acquireAttack(IPlayer player);

    /**
     * Chiede al giocatore quanti carri armati spostare dopo una conquista.
     *
     * @param player   il giocatore che ha conquistato
     * @param fromZone zona di partenza
     * @param toZone   zona appena conquistata
     * @param max      massimo numero di pedine spostabili
     * @return numero di pedine da spostare (compreso tra 1 e max)
     */
    int acquireTroopMovement(IPlayer player, String fromZone, String toZone, int max);

    /**
     * Chiede al giocatore di scegliere zona sorgente e zona destinazione per il
     * movimento strategico (fase finale del turno).
     *
     * @param player il giocatore che effettua il movimento
     * @return coppia [zonaOrigine, zonaDestinazione]; null se il giocatore rinuncia
     */
    String[] acquireStrategicMovement(IPlayer player);
}
