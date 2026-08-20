package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.dto.*;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployResponseRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Map;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    @Override
    public DeployResponseRisikoNewDTO sendInitialPlacementRequest(DeployRequestRisikoNewDTO request) {
        if (request == null || request.deployableZones() == null || request.deployableZones().isEmpty()) {
            return new DeployResponseRisikoNewDTO(Map.of());
        }
        String targetZone = request.deployableZones().stream().sorted().findFirst().orElse(null);
        if (targetZone == null) {
            return new DeployResponseRisikoNewDTO(Map.of());
        }
        return new DeployResponseRisikoNewDTO(Map.of(targetZone, request.tokenToDeploy().get(ERisikoNewToken.TANK)));
    }

    @Override
    public IDeployResponseDTO sendDeployRequest(IDeployRequestDTO request) {
        if (request == null || request.deployableZones() == null || request.deployableZones().isEmpty()) {
            return new DeployResponseRisikoNewDTO(Map.of());
        }
        String targetZone = request.deployableZones().stream().sorted().findFirst().orElse(null);
        return new DeployResponseRisikoNewDTO(Map.of());
    }

    @Override
    public AttackChoiceDTO sendAttackRequest(AttackRequestDTO request) {
        return new AttackChoiceDTO(null, null, 0);
    }

    @Override
    public FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request) {
        return new FortifyChoiceDTO(null, null, 0);
    }

}
