package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.turn.IGameCoordinator;

import java.util.List;

/**
 * Classe factory per la creazione dei manager di gioco
 */
public interface IGameFactory {
	
	IMediator getMediator();

	IGameCoordinator getGameCoordinator();

	List<IManager> getManagers();
}
