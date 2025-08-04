package model.board.risikoclassic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import model.IPlayer;
import model.board.IZone;
import model.utils.EnumRisikoClassic;

public class Continent implements IZone {
	
	private final String name;
	private List<IPlayer> players;
	private List<IZone> territories;
	private final EnumRisikoClassic type;
	private Integer armyValue;
	
	public Continent(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.territories = new ArrayList<>();
		this.type = EnumRisikoClassic.CONTINENTS;
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
	public EnumRisikoClassic getType() {
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
	public List<IZone> getNeighbours() {
		// TO DO
		return null;
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
	
	
}
