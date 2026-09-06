package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.dto.*;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployChoiceRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Map;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    @Override
    public DeployChoiceRisikoNewDTO sendInitialPlacementRequest(DeployRequestRisikoNewDTO request) {
        if (request == null || request.deployableZones() == null || request.deployableZones().isEmpty()) {
            return new DeployChoiceRisikoNewDTO(Map.of());
        }
        String targetZone = request.deployableZones().stream().sorted().findFirst().orElse(null);
        if (targetZone == null) {
            return new DeployChoiceRisikoNewDTO(Map.of());
        }
        return new DeployChoiceRisikoNewDTO(Map.of(targetZone, request.tokenToDeploy().get(ERisikoNewToken.TANK)));
    }

    @Override
    public AttackChoiceDTO sendAttackRequest(AttackRequestRisikoNewDTO request) {
        return null;
    }

    @Override
    public IDeployChoiceDTO sendDeployRequest(IDeployRequestDTO request) {
        if (request == null || request.deployableZones() == null || request.deployableZones().isEmpty()) {
            return new DeployChoiceRisikoNewDTO(Map.of());
        }
        String targetZone = request.deployableZones().stream().sorted().findFirst().orElse(null);
        return new DeployChoiceRisikoNewDTO(Map.of());
    }

    @Override
    public IAttackChoiceDTO sendAttackRequest(IAttackRequestDTO request) {
        return null;
    }

    @Override
    public FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request) {
        return new FortifyChoiceDTO(null, null, 0);
    }

}
