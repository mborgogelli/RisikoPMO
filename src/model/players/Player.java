package model.players;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.utils.EnumColors;
import model.utils.EnumToken;

public class Player implements IPlayer {

	private final String name;
	private Boolean isReady;
	private final EnumColors color;
	private final Map<EnumToken, Integer> tokens;
	
	public Player(String name, EnumColors color) {
		this.name = name;
		this.color = color;
		this.isReady = false;
		this.tokens = new HashMap<>();
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
	public Map<EnumToken, Integer> getAllTokens() {
		return Collections.unmodifiableMap(this.tokens);
	}
	
	@Override
	public Integer getToken(EnumToken token) {
		return tokens.getOrDefault(token, 0);
	}
	
	@Override
	public void addToken(EnumToken token, Integer amount) {
		if (amount > 0) {
			int current = this.getToken(token);
			this.setToken(token, amount + current);
		}
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
	
	private void setToken(EnumToken token, Integer amount) {
		this.tokens.computeIfAbsent(token, t -> new HashMap<EnumToken, Integer>().put(token, amount));
	}

	
}
