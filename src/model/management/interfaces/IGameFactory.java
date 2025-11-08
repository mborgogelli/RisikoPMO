package model.management.interfaces;

import java.util.List;

import model.management.Mediator;

/**
 * Classe factory per la creazione dei manager di gioco
 */
public interface IGameFactory {
	
	Mediator getMediator();
	
	List<IManager> getManagers();
}
