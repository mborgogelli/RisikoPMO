package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.dto.*;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployRequestDTO;

import java.util.Map;

public class GameCoordinatorRisikoNew implements IGameCoordinatorRisikoNew {

    @Override
    public Map<String, Integer> sendInitialPlacementRequest(InitialDeployRequestDTO request) {
        return null;
    }

    @Override
    public DeployResponseDTO sendDeployRequest(DeployRequestDTO request) {
        return null;
    }

    @Override
    public AttackChoiceDTO sendAttackRequest(AttackRequestDTO request) {
        return null;
    }

    @Override
    public FortifyChoiceDTO sendMoveRequest(FortifyRequestDTO request) {
        return null;
    }
}
