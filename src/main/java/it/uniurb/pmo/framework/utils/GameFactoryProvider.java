package it.uniurb.pmo.framework.utils;

import it.uniurb.pmo.framework.management.interfaces.IGameFactory;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;

public class GameFactoryProvider {
	
	public static IGameFactory getFactory(GameVersion version) {
        return switch (version) {
            case RISIKONEW -> new GameFactoryRisikoNew();
            default -> throw new IllegalArgumentException("Versione di gioco non supportata: " + version);
        };
    }

}
