package model.versions.risikockassic;

import model.board.IGameBoard;
import model.management.MapManager;
import model.utils.GameVersion;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class MapManagerRisikoNew extends MapManager {
	
	private static MapManagerRisikoNew instance;
	
	private IGameBoard gameBoard;
	
	private MapManagerRisikoNew() {
		super(GameVersion.RISIKOCLASSIC);
		this.gameBoard = super.getGameBoard();
	}
	
	public static MapManagerRisikoNew getInstance() {
		if (instance == null) {
			instance = new MapManagerRisikoNew();
		}
		return instance;
	}
	
	@Override
	public void resetInstance() {
		instance = null;
		this.gameBoard = super.getGameBoard();
	}

	@Override
	protected IGameBoard requestGameMap() {
        return BoardCreatorRisikoNew.getInstance().getMap();
	}    
	
	@Override
	public void initializeGameMap() {

	}
	
	@Override
	public IGameBoard getGameBoard() {
		if (this.gameBoard == null) {
			this.gameBoard = this.requestGameMap();
		}
		return this.gameBoard;
	}


}
