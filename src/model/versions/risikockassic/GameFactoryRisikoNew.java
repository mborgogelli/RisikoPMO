package model.versions.risikockassic;

import model.board.IBoardCreator;
import model.management.CardManager;
import model.management.MapManager;
import model.management.PhaseManager;
import model.management.TokenManager;
import model.management.interfaces.IGameFactory;
import model.utils.GameVersion;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;
import model.versions.risikockassic.managers.CardManagerRisikoNew;
import model.versions.risikockassic.managers.MapManagerRisikoNew;
import model.versions.risikockassic.managers.TankManager;

public class GameFactoryRisikoNew implements IGameFactory {

	@Override
	public IBoardCreator creteBoardCreator() {
		return BoardCreatorRisikoNew.getInstance();
	}

	@Override
	public MapManager createMapManager() {
		return MapManagerRisikoNew.getInstance();
	}

	@Override
	public TokenManager createTokenManager() {
		return TankManager.getInstance();
	}

	@Override
	public CardManager createCardManager() {
		return CardManagerRisikoNew.getInstance();
	}

	@Override
	public PhaseManager createTurnManager() {
		return null;
	}

}
