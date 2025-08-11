package model.versions.risikockassic.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import model.IPlayer;
import model.board.IZone;
import model.utils.GameVersion;
import model.versions.risikockassic.RisikoClassic;

public class Continent implements IZone {
	
	private final String name;
	private List<IPlayer> players;
	private List<IZone> territories;
	private final GameVersion type;
	private Integer armyValue;
	
	public Continent(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.territories = new ArrayList<>();
		this.type = RisikoClassic.CONTINENTS;
	}
	
	@Override
	public List<IPlayer> getOwners() {
		return this.players;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public GameVersion getType() {
		return this.type;
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
	public Boolean isControlledBy(IPlayer p) {
		Boolean isIn = false;
		if ((this.players.size() == 1) & (this.players.contains(p))) {
			isIn = true;
		}
		return isIn;
	}

	@Override
	public List<String> getNeighbours() {
		// TO DO
		return null;
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
		return this.armyValue;
	}

	@Override
	public void setValue(Integer value) {
		this.armyValue = value;		
	}

	@Override
	public void setNeighbours(List<String> neighbours) {
	}

	@Override
	public void setParentZone(IZone parent) {
		// TODO Auto-generated method stub
		
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
