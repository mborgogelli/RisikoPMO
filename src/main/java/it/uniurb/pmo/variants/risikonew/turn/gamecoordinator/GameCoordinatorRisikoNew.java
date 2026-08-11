package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.dto.AttackChoiceDTO;
import it.uniurb.pmo.variants.risikonew.dto.MoveChoiceDTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    @Override
    public Map<String, Integer> sendDeployRequest(IPlayer player, List<String> deployableZones, int toDeploy) {
        if (deployableZones == null || deployableZones.isEmpty()) {
            throw new IllegalArgumentException("No deployable zones available.");
        }
        if (toDeploy <= 0) {
            throw new IllegalArgumentException("No tanks available for deployment.");
        }
        String selectedZone = deployableZones.stream()
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalArgumentException("No deployable zones available."));
        Map<String, Integer> deployment = new HashMap<>();
        deployment.put(selectedZone, toDeploy);
        return deployment;
    }

    @Override
    public AttackChoiceDTO sendAttackRequest(IPlayer player, List<String> deployableZones) {
        return null;
    }

    @Override
    public MoveChoiceDTO sendMoveRequest(IPlayer player, List<String> deployableZones) {
        return null;
    }

    @Override
    public List<ICard> sendCardRedemptionRequest(IPlayer player, List<ICard> cards) {
        return Collections.emptyList();
    }
}
