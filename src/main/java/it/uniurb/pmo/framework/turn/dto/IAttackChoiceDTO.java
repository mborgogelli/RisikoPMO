package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;

public interface IAttackChoiceDTO extends IPlayerIdentityDTO {

    String getAttackerZone();

    String getDefenderZone();

    int getNumberOfDice();

    int getNumberOfUnits();

    ITokenType getTokenType();
}
