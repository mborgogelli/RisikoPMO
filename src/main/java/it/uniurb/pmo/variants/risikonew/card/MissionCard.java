package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.card.IMissionTarget;

public class MissionCard implements ICard {
	private final ERisikoNewMissionType missionType;
	private final IMissionTarget missionTarget;
	
	public MissionCard(ERisikoNewMissionType missionType, IMissionTarget missionTarget) {
		this.missionType = missionType;
		this.missionTarget = missionTarget;
	}
	
		
    @Override
   	public ICardType getCardType() {
		return ERisikoNewCardType.MISSION;
	}

}
