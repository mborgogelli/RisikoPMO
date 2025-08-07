package model.board.risikoclassic;

import java.util.List;

import model.board.IGameBoard;
import model.board.IZone;
import model.utils.GameVersion;

class GameBoardRisikoClassic implements IGameBoard {
	
	private List<IZone> continents;
	
	GameBoardRisikoClassic(List<IZone> continents) {
		this.continents = continents;
	}
	
	@Override
	public List<IZone> getZones() {
		return this.continents;
	}
	
	@Override
	public String getMapName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GameVersion getGameVersion() {
		return GameVersion.RISIKOCLASSIC;
	}
}
