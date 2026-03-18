package it.uniurb.pmo.model.players;

import java.util.Objects;

import it.uniurb.pmo.model.utils.EnumColors;

public class Player implements IPlayer {

	private final String name;
	private Boolean isReady;
	private EnumColors color;
	
	public Player(String name) {
		this.name = name;
		this.isReady = false;
	}
	
	
	public Player(String name, EnumColors color) {
		this.name = name;
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

	@Override
	public void setColor(EnumColors color) {
        this.color = color;		
	}

	@Override
	public void removeColor() {
		this.color = null;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", color=" + color + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(color, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return color == other.color && Objects.equals(name, other.name);
	}
	
}
