package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.utils.EColors;

import java.util.List;

public interface IGameState {

    String currentPlayer();

    EColors currentPlayerColor();

    List<String> playerOwnedZones();

    int currentTurn();

    int currentPhaseId();

}
