package model.utils;


/**
 * EnumToken contiene i token utilizzati nel gioco Risiko e dalle sue varianti
 */
public enum EnumToken {
	
	// RisikoNew tokens
	TANK(1),
	FLAG(10);
	
	private final Integer value;
	
	EnumToken(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
}
