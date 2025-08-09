package model.board.risikoclassic;

import java.util.List;

import model.board.IGameBoard;
import model.board.IZone;
import model.utils.GameVersion;
import static model.board.risikoclassic.RisikoClassic.*;

class GameBoardRisikoClassic implements IGameBoard {
	
	private List<IZone> continents;
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
	public List<IZone> getNeighbours(String zoneName) {
		// TODO Auto-generated method stub
		return null;
	}
}
