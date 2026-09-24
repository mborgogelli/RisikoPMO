package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.dto.FortifyChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.FortifyRequestDTO;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request);
}