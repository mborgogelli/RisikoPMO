package it.uniurb.pmo.variants.risikonew.turn.gamecoordinator;

import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployRequestDTO;

import java.util.Map;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    Map<String, Integer> sendInitialPlacementRequest(InitialDeployRequestDTO request);
}