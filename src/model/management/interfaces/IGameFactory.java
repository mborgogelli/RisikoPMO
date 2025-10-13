package model.management.interfaces;

import model.board.BoardCreator;
import model.management.CardManager;
import model.management.MapManager;
import model.management.PhaseManager;
import model.management.TokenManager;
import model.utils.GameVersion;

/**
 * Classe
 */
public interface IGameFactory {
	
	BoardCreator creteBoardCreator(GameVersion version);
	MapManager createMapManager();
    TokenManager createTokenManager();
    CardManager createCardManager();
    PhaseManager createTurnManager();
}
