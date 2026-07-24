package it.uniurb.pmo.variants.risikonew;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.IPlayerInputProvider;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInputProviderRisikoNew implements IPlayerInputProvider {

    @Override
    public Map<String, Map<ITokenType, Integer>> acquireDeployment(
            IPlayer player,
            List<String> deployableZones,
            Map<ITokenType, Integer> availableTokens
    ) {
        if (deployableZones == null || deployableZones.isEmpty()) {
            throw new IllegalArgumentException("No deployable zones available.");
        }
        if (availableTokens == null || availableTokens.isEmpty()) {
            throw new IllegalArgumentException("No available tokens provided.");
        }

        String selectedZone = deployableZones.stream()
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalArgumentException("No deployable zones available."));
        int availableTanks = availableTokens.getOrDefault(ERisikoNewToken.TANK, 0);
        if (availableTanks <= 0) {
            throw new IllegalArgumentException("No tanks available for deployment.");
        }

        Map<String, Map<ITokenType, Integer>> deployment = new HashMap<>();
        deployment.put(selectedZone, Map.of(ERisikoNewToken.TANK, availableTanks));
        return deployment;
    }

    @Override
    public String[] acquireAttack(IPlayer player) {
        throw new UnsupportedOperationException("Attack input is not implemented yet.");
    }

    @Override
    public int acquireTroopMovement(IPlayer player, String fromZone, String toZone, int max) {
        throw new UnsupportedOperationException("Troop movement input is not implemented yet.");
    }

    @Override
    public String[] acquireStrategicMovement(IPlayer player) {
        throw new UnsupportedOperationException("Strategic movement input is not implemented yet.");
    }
}
