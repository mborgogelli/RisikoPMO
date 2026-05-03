package it.uniurb.pmo.framework.card;

import it.uniurb.pmo.framework.players.Player;

public interface IMissionCard extends ICard<IMissionType, MissionObjective> {

    boolean isAchievementReached(Player player);
}
