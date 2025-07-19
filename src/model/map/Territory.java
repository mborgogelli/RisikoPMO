package model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import model.IPlayer;
import model.utils.ZoneType;

public class Territory implements IZone {

	private final String name;
	private List<IPlayer> players;
	private List<IZone> neighbours;
	private final ZoneType type;
	
	public Territory(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.neighbours = new ArrayList<>();
		this.type = ZoneType.CONTINENTS;
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
	public ZoneType getType() {
		return this.type;
	}

	@Override
	public Optional<IZone> getParentZone() {
		return Optional.empty();
	}
	
	@Override
	public List<IZone> getChildZones() {
		// TO DO
		return null;
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
		return this.neighbours;
	}
}
