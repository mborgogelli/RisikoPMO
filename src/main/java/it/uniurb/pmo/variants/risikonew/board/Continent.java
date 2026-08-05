package it.uniurb.pmo.variants.risikonew.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.uniurb.pmo.framework.board.IZone;

public class Continent implements IZone {
	
	private final String name;
	private final List<IZone> territories;
	private Integer armyBonus;
	
	public Continent(String name) {
		this.name = name;
		this.territories = new ArrayList<>();
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Optional<IZone> getParentZone() {
		return Optional.empty();
	}
	
	@Override
	public List<IZone> getChildZones() {
		return this.territories;
	}

	@Override
	public List<String> getNeighbours() {
		List<String> territories = this.territories.stream()
	            .map(IZone::getName)
	            .toList();
	    return this.territories.stream()
	            .flatMap(zone -> zone.getNeighbours().stream())
	            .filter(neighbourName -> !territories.contains(neighbourName))
	            .distinct()
	            .toList();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setChildZones(List<IZone> zone) {
		this.territories.addAll(zone);
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
	}

	@Override
	public void setParentZone(IZone parent) {
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
		Continent other = (Continent) obj;
		return Objects.equals(name, other.name);
	}
	
}
