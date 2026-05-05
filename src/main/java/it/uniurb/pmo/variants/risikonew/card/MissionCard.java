package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.IMissionTarget;

public class MissionCard implements ICard<EnumMissionTypeRisikoNew, IMissionTarget> {
	private final EnumMissionTypeRisikoNew missionType;
	private final IMissionTarget missionTarget;
	
	public MissionCard(EnumMissionTypeRisikoNew missionType, IMissionTarget missionTarget) {
		this.missionType = missionType;
		this.missionTarget = missionTarget;
	}
	
		
    @Override
    public EnumMissionTypeRisikoNew getCardType() {
        return this.missionType;
    }

    @Override
    public IMissionTarget getCardContent() {
        return this.missionTarget;
    }
}
