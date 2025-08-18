package model.players;

import model.utils.EnumColors;

public class Player implements IPlayer {
	
	private final String name;
	private Boolean isReady;
	private final EnumColors color;
	
	public Player(String name, EnumColors color) {
		this.name = name;
		this.color = color;
		this.isReady = false;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void setReady(Boolean ready) {
		this.isReady = ready;
	}

	@Override
	public EnumColors getColor() {
		return this.color;
	}
	
	
}
