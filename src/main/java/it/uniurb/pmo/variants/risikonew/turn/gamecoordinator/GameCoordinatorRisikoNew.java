package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.dto.*;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployResponseDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployRequestDTO;

import java.util.List;
import java.util.Map;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    @Override
    public InitialDeployResponseDTO sendInitialPlacementRequest(InitialDeployRequestDTO request) {
        if (request == null || request.getDeployableZones() == null || request.getDeployableZones().isEmpty()) {
            return new InitialDeployResponseDTO(Map.of());
        }
        String targetZone = request.getDeployableZones().stream().sorted().findFirst().orElse(null);
        if (targetZone == null) {
            return new InitialDeployResponseDTO(Map.of());
        }
        return new InitialDeployResponseDTO(Map.of(targetZone, request.tankToDeploy()));
    }

    @Override
    public DeployResponseDTO sendDeployRequest(DeployRequestDTO request) {
        if (request == null || request.getDeployableZones() == null || request.getDeployableZones().isEmpty()) {
            return new DeployResponseDTO(Map.of());
        }
        String targetZone = request.getDeployableZones().stream().sorted().findFirst().orElse(null);
        if (targetZone == null) {
            return new DeployResponseDTO(Map.of());
        }
        return new DeployResponseDTO(Map.of(targetZone, Map.copyOf(request.getTokenDeploy())));
    }

    @Override
    public AttackChoiceDTO sendAttackRequest(AttackRequestDTO request) {
        List<String> zones = request == null ? List.of() : request.getOwnedZones();
        String attacker = zones.isEmpty() ? "" : zones.get(0);
        String defender = zones.size() > 1 ? zones.get(1) : attacker;
        return new AttackChoiceDTO(attacker, defender, 1);
    }

    @Override
    public FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request) {
        List<String> zones = request == null ? List.of() : request.getOwnedZones();
        String fromZone = zones.isEmpty() ? "" : zones.get(0);
        String toZone = zones.size() > 1 ? zones.get(1) : fromZone;
        return new FortifyChoiceDTO(fromZone, toZone, 1);
    }
}
