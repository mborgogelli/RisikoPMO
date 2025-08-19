package model.utils;

import java.util.List;

/**
 * EnumToken contiene i token utilizzati nel gioco Risiko e dalle sue varianti
 */
public enum EnumToken {
	
	TANK(1),
	FLAG(10);
	
	private final Integer value;
	
	EnumToken(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
	public static List<EnumToken> getRisikoNewTokens(){
		return List.of(TANK, FLAG);
	}
}
