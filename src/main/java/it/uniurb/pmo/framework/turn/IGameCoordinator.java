package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.turn.dto.*;

/**
 * Canale di comunicazione tra la logica di gioco e i giocatori.
 * Il AbstractGameCoordinator si occupa di inviare messaggi/richieste al giocatore
 * e di raccogliere la risposta, mantenendo il framework agnostico rispetto
 * alla variante di gioco concreta.
 */
public interface IGameCoordinator {

    /**
     * Chiede al giocatore dove distribuire un certo tipo di token disponibili.
     *
     * @param request il DTO contenente le informazioni sulla distribuzione
     * @return la scelta di distribuzione del giocatore
     */
    DeployResponseDTO sendDeployRequest(DeployRequestDTO request);

    /**
     * Chiede al giocatore se e dove attaccare.
     *
     * @param request il DTO contenente le informazioni sull'attacco
     * @return la scelta di attacco del giocatore
     */
    AttackChoiceDTO sendAttackRequest(AttackRequestDTO request);

    /**
     * Chiede al giocatore se e dove spostare truppe.
     *
     * @param request il DTO contenente le informazioni sullo spostamento
     * @return la scelta di spostamento del giocatore
     */
    FortifyChoiceDTO sendMoveRequest(FortifyRequestDTO request);
}
