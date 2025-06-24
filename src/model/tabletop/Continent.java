package model.tabletop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import model.IPlayer;

public class Continent implements IZone {
	
	private final String name;
	private List<IPlayer> players;
	
	public Continent(String name) {
		this.name = name;
		this.players = new ArrayList<>();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<IZone> getParentZone() {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Set<IZone> getChildZones() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isControlledBy(IPlayer p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IZone> getBorders() {
		// TODO Auto-generated method stub
		return null;
	}

}
