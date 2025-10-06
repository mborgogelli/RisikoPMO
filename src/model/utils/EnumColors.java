package model.utils;

import java.util.EnumSet;

/**
 * EnumColors contiene i colori utilizzati nel gioco Risiko e dalle sue varianti
 */
public enum EnumColors {
	
	BLACK, BLUE, GREEN, PURPLE, RED, YELLOW;
	
	public EnumSet<EnumColors> getAvailableColors(){
		return EnumSet.allOf(EnumColors.class);
	}
}
