package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;

import java.util.Map;

/**
 * DTO di risposta per la fase di dispiegamento (deployment).
 * Contiene la scelta del giocatore: mappa territorio → (tipoPedina → quantità piazzata).
 */
public class DeployResponseDTO {

    private final Map<String, Map<ITokenType, Integer>> deployment;

    public DeployResponseDTO(Map<String, Map<ITokenType, Integer>> deployment) {
        this.deployment = deployment;
    }

    public Map<String, Map<ITokenType, Integer>> getDeployment() {
        return deployment;
    }
}
