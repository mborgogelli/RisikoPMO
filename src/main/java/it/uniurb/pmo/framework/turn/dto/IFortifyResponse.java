package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.turn.IPlayerDataDTO;

public interface IFortifyResponse extends IPlayerDataDTO {

    int getTotalReinforcements();

    int reinforcementsByOwnedZones();

    int reinforcementsByOwnedZonesBonus();

    int reinforcementsByCards();
}
