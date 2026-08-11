package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.variants.risikonew.dto.AttackChoiceDTO;
import it.uniurb.pmo.variants.risikonew.dto.MoveChoiceDTO;

import java.util.List;
import java.util.Map;

public class AbstractGameCoordinator implements IGameCoordinator {

    @Override
    public Map<String, Integer> sendDeployRequest(IPlayer player, List<String> deployableZones, ITokenType tokenType, int toDeploy) {
        return Map.of();
    }

    @Override
    public AttackChoiceDTO sendAttackRequest(IPlayer player, List<String> deployableZones) {
        return null;
    }

    @Override
    public MoveChoiceDTO sendMoveRequest(IPlayer player, List<String> deployableZones) {
        return null;
    }
}
