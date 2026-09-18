package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.turn.command.IGameCommandPublisher;
import it.uniurb.pmo.framework.turn.dto.IAttackChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IDeployChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.event.IGameEventReceiver;

import java.util.Optional;

/**
 * Canale di comunicazione tra la logica di gioco e i giocatori.
 * Il AbstractGameCoordinator si occupa di inviare messaggi/richieste al giocatore
 * e di raccogliere la risposta, mantenendo il framework agnostico rispetto
 * alla variante di gioco concreta.
 */
public interface IGameCoordinator extends IGameEventReceiver, IGameCommandPublisher {

    /**
     * Chiede al giocatore dove distribuire un certo tipo di token disponibili.
     *
     * @param request il DTO contenente le informazioni sulla distribuzione
     * @return la scelta di distribuzione del giocatore
     */
    IDeployChoiceDTO sendDeployRequest(IDeployRequestDTO request);

    Optional<IAttackChoiceDTO> sendAttackRequest(IAttackRequestDTO request);

}
