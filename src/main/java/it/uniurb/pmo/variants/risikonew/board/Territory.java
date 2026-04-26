package it.uniurb.pmo.variants.risikonew.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.uniurb.pmo.framework.board.IZone;

public class Territory implements IZone {

	private final String name;
	private final List<String> neighbours;
	private IZone parentZone;
	private Integer armyBonus;
	
	public Territory(String name) {
		this.name = name;
		this.neighbours = new ArrayList<>();
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Optional<IZone> getParentZone() {
		return Optional.ofNullable(this.parentZone);
	}
	
	@Override
	public List<IZone> getChildZones() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getNeighbours() {
		return this.neighbours;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public void setChildZones(List<IZone> zone) {
	}

	@Override
	public Integer getValue() {
		return this.armyBonus;
	}

	@Override
	public void setValue(Integer value) {
		this.armyBonus = value;
	}

	@Override
	public void setNeighbours(List<String> neighbours) {
			this.neighbours.addAll(neighbours);
	}

	@Override
	public void setParentZone(IZone parent) {
		this.parentZone = parent;	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Territory other = (Territory) obj;
		return Objects.equals(name, other.name);
	}
	
}
