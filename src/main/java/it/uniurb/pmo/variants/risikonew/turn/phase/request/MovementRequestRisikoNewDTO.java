package it.uniurb.pmo.variants.risikonew.turn.phase.request;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.phase.request.IMovementRequest;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MovementRequestRisikoNewDTO(Map<String, List<String>> destinations,
                                          Map<String, Integer> moveableTanks)
        implements IMovementRequest {


    @Override
    public Map<String, Map<ITokenType, Integer>> moveableTokens() {
        return this.moveableTanks.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> Map.of(ERisikoNewToken.TANK, entry.getValue())));
    }
}
