package model.tabletop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import model.IPlayer;

public class Continent implements IZone {
	
	private final String name;
	private List<IPlayer> players;
	private Set<IZone> territories;
	private final ZoneType type;
	
	public Continent(String name) {
		this.name = name;
		this.players = new ArrayList<>();
		this.territories = new HashSet<>();
		this.type = ZoneType.CONTINENTE;
	}
	
	@Override
	public List<IPlayer> getOwner() {
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
	public Set<IZone> getChildZones() {
		// TO DO
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
	public Set<IZone> getNeighbours() {
		// TODO
		return null;
	}

}
