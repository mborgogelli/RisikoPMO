package model.versions.risikockassic.board;

import static model.versions.risikockassic.RisikoClassic.*;

import java.util.List;

import model.board.IGameBoard;
import model.board.IZone;
import model.utils.GameVersion;

class GameBoardRisikoClassic implements IGameBoard {
	
	private final List<IZone> continents;
	private final GameVersion gameVersion;
	
	GameBoardRisikoClassic(List<IZone> continents) {
		this.continents = continents;
		this.gameVersion = RISIKOCLASSIC;
	}
	
	@Override
	public List<IZone> getZones() {
		return this.continents;
	}
	
	@Override
	public GameVersion getGameVersion() {
		return this.gameVersion;
	}

	@Override
	public IZone findZoneByName(String zoneName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getNeighbours(String zoneName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IZone whereIsZone(String zoneName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canReach(String zoneTo, String zoneFrom) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getValue(String zoneName) {
		// TODO Auto-generated method stub
		return null;
	}
}
