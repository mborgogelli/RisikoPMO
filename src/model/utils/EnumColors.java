package model.utils;

import java.util.EnumSet;

public enum EnumColors {
	
	BLACK, BLUE, GREEN, PURPLE, RED, YELLOW;
	
	public EnumSet<EnumColors> getAvailableColors(){
		return EnumSet.allOf(EnumColors.class);
	}
}
