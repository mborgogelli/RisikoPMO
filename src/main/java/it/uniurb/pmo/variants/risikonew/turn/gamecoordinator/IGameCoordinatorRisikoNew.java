package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.dto.AttackChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.AttackRequestDTO;
import it.uniurb.pmo.framework.turn.dto.FortifyChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.FortifyRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.DeployResponseRisikoNewDTO;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    DeployResponseRisikoNewDTO sendInitialPlacementRequest(DeployRequestRisikoNewDTO request);

    AttackChoiceDTO sendAttackRequest(AttackRequestDTO request);

    FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request);
}