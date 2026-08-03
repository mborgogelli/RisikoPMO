package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.dto.AttackChoiceDTO;
import it.uniurb.pmo.variants.risikonew.dto.MoveChoiceDTO;

import java.util.List;
import java.util.Map;

/**
 * Canale di comunicazione tra la logica di gioco e i giocatori.
 *
 * Il GameCoordinator si occupa di inviare messaggi/richieste al giocatore
 * e di raccogliere la risposta, mantenendo il framework agnostico rispetto
 * alla variante di gioco concreta.
 */
public interface IGameCoordinator {

    /**
     * Chiede al giocatore dove distribuire i tank disponibili.
     *
     * @param player          il giocatore che deve effettuare il deploy
     * @param deployableZones lista delle zone in cui può deployare
     * @param toDeploy        numero di tank da distribuire
     * @return mappa zona → numero di tank da piazzare
     */
    Map<String, Integer> sendDeployRequest(IPlayer player, List<String> deployableZones, int toDeploy);

    /**
     * Chiede al giocatore se e dove attaccare.
     *
     * @param player          il giocatore attaccante
     * @param deployableZones lista delle zone attaccabili
     * @return la scelta di attacco del giocatore
     */
    AttackChoiceDTO sendAttackRequest(IPlayer player, List<String> deployableZones);

    /**
     * Chiede al giocatore se e dove spostare truppe.
     *
     * @param player          il giocatore che effettua lo spostamento
     * @param deployableZones lista delle zone da cui può spostare
     * @return la scelta di spostamento del giocatore
     */
    MoveChoiceDTO sendMoveRequest(IPlayer player, List<String> deployableZones);
}
