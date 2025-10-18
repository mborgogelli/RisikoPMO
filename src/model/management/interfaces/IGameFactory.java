package model.management.interfaces;

import model.management.CardManager;
import model.management.PhaseManager;
import model.management.TokenManager;

/**
 * Classe
 */
public interface IGameFactory {
	
	IMapManager createMapManager();
    TokenManager createTokenManager();
    CardManager createCardManager();
    PhaseManager createTurnManager();
	IRuleManager createRuleManager();
}
