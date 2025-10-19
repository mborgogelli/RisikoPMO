package model.management.interfaces;

import java.util.List;

import model.management.Mediator;

/**
 * Classe
 */
public interface IGameFactory {
	
	Mediator getMediator();
	
	List<IManager> getManagers();
}
