package model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import model.IPlayer;
import model.utils.ZoneType;

public class Continent implements IZone {
	
	private final String name;
	private List<IPlayer> players;
	private List<IZone> territories;
	private final ZoneType type;
	
	public Continent(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.territories = new ArrayList<>();
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

}
