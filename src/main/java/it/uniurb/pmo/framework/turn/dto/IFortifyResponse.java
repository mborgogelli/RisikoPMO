package it.uniurb.pmo.framework.turn.dto;

public interface IFortifyResponse extends IPlayerDataDTO {

    int getTotalReinforcements();

    int reinforcementsByOwnedZones();

    int reinforcementsByOwnedZonesBonus();

    int reinforcementsByCards();
}
