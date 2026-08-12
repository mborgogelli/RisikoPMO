package it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.dto.DeployRequestDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import java.util.List;
import java.util.Map;

public class InitialDeployRequestDTO extends DeployRequestDTO {

    public InitialDeployRequestDTO(IPlayer player, List<String> deployableZones, int tokensToDeploy) {
        super(player, deployableZones, Map.of(ERisikoNewToken.TANK, tokensToDeploy));
    }

    public int tankToDeploy() {
        return super.getTokenDeploy().get(ERisikoNewToken.TANK);
    }
}
