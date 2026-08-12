package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployResponseDTO;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    InitialDeployResponseDTO sendInitialPlacementRequest(InitialDeployRequestDTO request);
}