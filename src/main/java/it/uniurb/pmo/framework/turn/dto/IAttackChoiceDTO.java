package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IPlayerDataDTO;

public interface IAttackChoiceDTO extends IPlayerDataDTO {

    String getAttackerZone();

    String getDefenderZone();

    int getNumberOfDice();

    int getNumberOfUnits();

    ITokenType getTokenType();
}
