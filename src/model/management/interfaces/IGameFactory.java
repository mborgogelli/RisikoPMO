package model.management.interfaces;

import model.board.IBoardCreator;
import model.management.CardManager;
import model.management.MapManager;
import model.management.PhaseManager;
import model.management.TokenManager;
import model.utils.GameVersion;

/**
 * Classe
 */
public interface IGameFactory {
	
	IBoardCreator creteBoardCreator();
	IMapManager createMapManager();
    TokenManager createTokenManager();
    CardManager createCardManager();
    PhaseManager createTurnManager();
	IGameRuleManager createRuleManager();
}
