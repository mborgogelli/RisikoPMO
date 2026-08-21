package it.uniurb.pmo.framework.utils;

import java.util.EnumSet;
import java.util.List;

/**
 * EColors contiene i colori utilizzati nel gioco Risiko e dalle sue varianti
 */
public enum EColors {
	
	BLACK, BLUE, GREEN, PURPLE, RED, YELLOW;
	
	public static List<EColors> getAvailableColors(){
		return EnumSet.allOf(EColors.class).stream().toList();
	}
}
