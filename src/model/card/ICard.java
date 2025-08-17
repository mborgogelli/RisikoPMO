package model.card;

import model.board.IZone;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, come azioni speciali o bonus.
 * 
 */


public interface ICard {
	
	IZone getZone();
	
	ISymbolCard getSymbol();  
	
}
