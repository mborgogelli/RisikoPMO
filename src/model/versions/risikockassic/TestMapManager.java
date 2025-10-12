package model.versions.risikockassic;

import java.util.List;

import model.board.BoardCreator;
import model.board.IZone;
import model.management.MapManager;
import model.players.IPlayer;
import model.players.Player;

public class TestMapManager extends MapManager implements IMapManagerRisikoNew {

	public TestMapManager(BoardCreator boardCreator) {
		super(boardCreator);
	}

	@Override
	public boolean canMoveBetween(IPlayer player, String toTerritory, String fromTerritory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IZone> getZonesOwnedBy(IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateZoneOwnership(IPlayer newOwner, IZone territory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isReady() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initPlayerZones(List<IPlayer> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IZone> getNeighboursNotOwnedBy(String territoryName, IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}



}
