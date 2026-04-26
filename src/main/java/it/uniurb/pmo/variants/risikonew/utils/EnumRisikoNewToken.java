package it.uniurb.pmo.variants.risikonew.utils;


/**
 * EnumRisikoNewToken contiene i token utilizzati nel gioco Risiko e dalle sue varianti
 */
public enum EnumRisikoNewToken {
	
	// RisikoNew tokens
	TANK(1),
	FLAG(10);
	
	private final Integer value;
	
	EnumRisikoNewToken(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
}
