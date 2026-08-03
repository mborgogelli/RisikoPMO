package it.uniurb.pmo.framework.turn;

/**
 * Canale di comunicazione tra la logica di gioco e i giocatori.
 *
 * Il GameCoordinator si occupa di inviare messaggi/richieste al giocatore
 * (request DTO) e di raccogliere la risposta (response DTO), mantenendo
 * il framework agnostico rispetto alla variante di gioco concreta.
 *
 * @param <REQ>  tipo del DTO di richiesta inviato al giocatore
 * @param <RES>  tipo del DTO di risposta ricevuto dal giocatore
 */
public interface IGameCoordinator<REQ, RES> {

    /**
     * Invia una richiesta al giocatore e attende la sua risposta.
     *
     * @param request il DTO che descrive il contesto e l'azione richiesta
     * @return il DTO con la scelta effettuata dal giocatore
     */
    RES sendRequest(REQ request);
}
