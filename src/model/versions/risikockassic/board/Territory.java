package model.versions.risikockassic.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import model.IPlayer;
import model.board.IZone;
import model.utils.GameVersion;
import model.versions.risikockassic.RisikoClassic;

public class Territory implements IZone {

	private final String name;
	private final GameVersion type;
	private final List<IPlayer> players;
	private final List<String> neighbours;
	private IZone parentZone;
	private Integer armyValue;
	
	public Territory(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.neighbours = new ArrayList<>();
		this.type = RisikoClassic.TERRITORIES;
	}
	
	@Override
	public List<IPlayer> getOwners() {
		return Collections.unmodifiableList(this.players);
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
		return Optional.ofNullable(this.parentZone);
	}
	
	@Override
	public List<IZone> getChildZones() {
		return Collections.emptyList();
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
		return this.armyValue;
	}

	@Override
	public void setValue(Integer value) {
		this.armyValue = value;
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
