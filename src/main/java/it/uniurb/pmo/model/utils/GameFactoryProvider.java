package it.uniurb.pmo.model.utils;

import it.uniurb.pmo.model.management.interfaces.IGameFactory;
import it.uniurb.pmo.model.versions.risikockassic.GameFactoryRisikoNew;

public class GameFactoryProvider {
	
	public static IGameFactory getFactory(GameVersion version) {
        switch(version) {
            case RISIKONEW:
                return new GameFactoryRisikoNew();
            default:
                throw new IllegalArgumentException("Versione di gioco non supportata: " + version);
        }
    }
}
