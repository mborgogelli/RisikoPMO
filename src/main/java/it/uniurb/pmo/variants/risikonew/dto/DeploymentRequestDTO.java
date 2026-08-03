package it.uniurb.pmo.variants.risikonew.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;

import java.util.List;
import java.util.Map;

/**
 * DTO di richiesta per la fase di dispiegamento (deployment).
 * Contiene il contesto necessario al giocatore per scegliere dove piazzare le pedine.
 */
public class DeploymentRequestDTO {

    private final IPlayer player;
    private final List<String> deployableZones;
    private final Map<ITokenType, Integer> availableTokens;

    public DeploymentRequestDTO(IPlayer player, List<String> deployableZones, Map<ITokenType, Integer> availableTokens) {
        this.player = player;
        this.deployableZones = deployableZones;
        this.availableTokens = availableTokens;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public List<String> getDeployableZones() {
        return deployableZones;
    }

    public Map<ITokenType, Integer> getAvailableTokens() {
        return availableTokens;
    }
}
