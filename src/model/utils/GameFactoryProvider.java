package model.utils;

import model.management.interfaces.IGameFactory;
import model.versions.risikockassic.GameFactoryRisikoNew;

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
