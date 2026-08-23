package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.ICardManager;
import it.uniurb.pmo.framework.players.IPlayer;

public interface ICardManagerRisikoNew extends ICardManager {
    int getBestReinforcementByCards(IPlayer player);


	
}
