package model.utils;

import java.util.EnumSet;
import java.util.List;

/**
 * EnumColors contiene i colori utilizzati nel gioco Risiko e dalle sue varianti
 */
public enum EnumColors {
	
	BLACK, BLUE, GREEN, PURPLE, RED, YELLOW;
	
	public static List<EnumColors> getAvailableColors(){
		return EnumSet.allOf(EnumColors.class).stream().toList();
	}
}
