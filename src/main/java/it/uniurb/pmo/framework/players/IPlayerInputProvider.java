package it.uniurb.pmo.framework.players;

import java.util.Map;

/**
 * Canale bidirezionale tra la logica di gioco e il giocatore.
 *
 * I metodi acquire* bloccano il thread di gioco finché il giocatore non risponde.
 * I metodi submit* sono chiamati dal layer REST/WebSocket per consegnare la risposta
 * e sbloccare il thread in attesa.
 *
 * Non contiene logica di gioco: le validazioni restano nei manager e nelle fasi.
 * Non estende IMediator: è un componente autonomo iniettato nel mediatore.
 */
public interface IPlayerInputProvider {

    // ── Metodi di acquisizione (chiamati dalla logica di gioco) ──────────────

    /**
     * Chiede al giocatore come vuole distribuire le pedine tra i territori.
     * Il thread si blocca finché il giocatore non risponde tramite submitDeployment.
     *
     * @param player          il giocatore che deve dispiegare
     * @param availableTokens mappa tipoPedina → quantità disponibile da piazzare
     * @return mappa territorio → (tipoPedina → quantità piazzata in quel territorio)
     */
    Map<String, Map<ITokenType, Integer>> acquireDeployment(IPlayer player, Map<ITokenType, Integer> availableTokens);

    /**
     * Chiede al giocatore di scegliere la zona da cui attaccare e quella da attaccare.
     * Il thread si blocca finché il giocatore non risponde tramite submitAttack.
     *
     * @param player il giocatore attaccante
     * @return coppia [zonaAttaccante, zonaBersaglio]; null se il giocatore rinuncia ad attaccare
     */
    String[] acquireAttack(IPlayer player);

    /**
     * Chiede al giocatore quanti carri armati spostare dopo una conquista.
     * Il thread si blocca finché il giocatore non risponde tramite submitTroopMovement.
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
     * Il thread si blocca finché il giocatore non risponde tramite submitStrategicMovement.
     *
     * @param player il giocatore che effettua il movimento
     * @return coppia [zonaOrigine, zonaDestinazione]; null se il giocatore rinuncia
     */
    String[] acquireStrategicMovement(IPlayer player);

    // ── Metodi di risposta (chiamati dal layer REST/WebSocket) ───────────────

    /**
     * Consegna la scelta di dispiegamento del giocatore e sblocca il thread in attesa.
     * Chiamato dal controller REST quando il client invia la propria scelta.
     *
     * @param playerName nome del giocatore che ha risposto
     * @param choice     mappa territorio → (tipoPedina → quantità)
     */
    void submitDeployment(String playerName, Map<String, Map<ITokenType, Integer>> choice);

    /**
     * Consegna la scelta di attacco del giocatore e sblocca il thread in attesa.
     * Chiamato dal controller REST quando il client invia la propria scelta.
     *
     * @param playerName nome del giocatore che ha risposto
     * @param choice     [zonaAttaccante, zonaBersaglio]; null se rinuncia ad attaccare
     */
    void submitAttack(String playerName, String[] choice);

    /**
     * Consegna il numero di pedine da spostare dopo la conquista e sblocca il thread.
     * Chiamato dal controller REST quando il client invia la propria scelta.
     *
     * @param playerName nome del giocatore che ha risposto
     * @param count      numero di pedine da spostare
     */
    void submitTroopMovement(String playerName, int count);

    /**
     * Consegna la scelta di movimento strategico del giocatore e sblocca il thread.
     * Chiamato dal controller REST quando il client invia la propria scelta.
     *
     * @param playerName nome del giocatore che ha risposto
     * @param choice     [zonaOrigine, zonaDestinazione]; null se rinuncia al movimento
     */
    void submitStrategicMovement(String playerName, String[] choice);
}
