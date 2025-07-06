package model.map;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import model.IPlayer;
import model.utils.ZoneType;

public class Territory implements IZone {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
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
	public List<IZone> getChildZones() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPlayer> getOwners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isControlledBy(IPlayer p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IZone> getNeighbours() {
		// TODO Auto-generated method stub
		return null;
	}

}
