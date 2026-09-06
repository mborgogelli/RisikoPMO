package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.dto.AttackChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.FortifyChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.FortifyRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployChoiceRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployRequestRisikoNewDTO;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    DeployChoiceRisikoNewDTO sendInitialPlacementRequest(DeployRequestRisikoNewDTO request);

    AttackChoiceDTO sendAttackRequest(AttackRequestRisikoNewDTO request);

    FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request);
}