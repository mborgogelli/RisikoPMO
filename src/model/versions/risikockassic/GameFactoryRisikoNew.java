package model.versions.risikockassic;

import model.board.IBoardCreator;
import model.management.CardManager;
import model.management.PhaseManager;
import model.management.TokenManager;
import model.management.interfaces.IGameFactory;
import model.management.interfaces.IRuleManager;
import model.management.interfaces.IMapManager;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;
import model.versions.risikockassic.interfaces.IMapManagerRisikoNew;
import model.versions.risikockassic.managers.CardManagerRisikoNew;
import model.versions.risikockassic.managers.MapManagerRisikoNew;
import model.versions.risikockassic.managers.RuleManagerRisikoNew;
import model.versions.risikockassic.managers.TankManager;

public class GameFactoryRisikoNew implements IGameFactory {
	
	private final IRuleManager mediator = new RuleManagerRisikoNew();
	
	@Override
	public IRuleManager createRuleManager() {
	    return this.mediator;
	}
	
	@Override
	public IMapManagerRisikoNew createMapManager() {
		return new MapManagerRisikoNew();
	}

	@Override
	public TokenManager createTokenManager() {
		return new TankManager();
	}

	@Override
	public CardManager createCardManager() {
		return new CardManagerRisikoNew(this.mediator);
	}

	@Override
	public PhaseManager createTurnManager() {
		return null;
	}
	
	
	
}