package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IPlayerRequestDTO;

import java.util.List;
import java.util.Map;

/**
 * DTO di richiesta per la fase di dispiegamento (deployment).
 * Contiene il contesto necessario al giocatore per scegliere dove piazzare le pedine.
 */
public abstract class DeployRequestDTO implements IPlayerRequestDTO {

    protected final IPlayer player;
    protected final List<String> deployableZones;
    protected final Map<ITokenType, Integer> tokensToDeploy;

    public DeployRequestDTO(IPlayer player, List<String> deployableZones, Map<ITokenType, Integer> tokensToDeploy) {
        this.player = player;
        this.deployableZones = deployableZones;
        this.tokensToDeploy = tokensToDeploy;
    }

    public List<String> getDeployableZones() {
        return deployableZones;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public Map<ITokenType, Integer> getTokenDeploy() {
        return tokensToDeploy;
    }
}
