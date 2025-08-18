package model.card;

import model.board.IZone;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, come azioni speciali o bonus.
 * 
 */


public interface ICard {
	/**
	 * Restituisce la zona associata alla carta.
	 * Ad esempio, una carta territorio restituirà il territorio associato.
	 * 
	 * @return la zona associata alla carta, non può essere null
	 */
	IZone getZone();
	
	/**
	 * Restituisce il simbolo associato alla carta se presente
	 * Ad esempio, una carta territorio avrà un simbolo che rappresente Infantry, Artillery, Cavalry o Jolly.
	 * 
	 * @return il simbolo della carta, non può essere null
	 */
	ISymbolCard getSymbol();  
	
}
