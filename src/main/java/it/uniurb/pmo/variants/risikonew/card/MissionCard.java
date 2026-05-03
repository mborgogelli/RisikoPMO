package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.IMissionCard;
import it.uniurb.pmo.framework.card.IMissionType;
import it.uniurb.pmo.framework.card.MissionObjective;
import it.uniurb.pmo.framework.players.Player;

public class MissionCard implements IMissionCard {

    @Override
    public boolean isAchievementReached(Player player) {
        return false;
    }

    @Override
    public IMissionType getCardType() {
        return null;
    }

    @Override
    public MissionObjective getCardContent() {
        return null;
    }
}
