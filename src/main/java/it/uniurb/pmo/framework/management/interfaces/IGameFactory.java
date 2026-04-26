package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.management.AbstractMediator;

/**
 * Classe factory per la creazione dei manager di gioco
 */
public interface IGameFactory {
	
	AbstractMediator getMediator();
	
	List<IManager> getManagers();
}
