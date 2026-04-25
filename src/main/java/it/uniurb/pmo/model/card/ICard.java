package it.uniurb.pmo.model.card;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, inclusi bonus, azioni speciali o rappresentare territori.
 * La struttura specifica e le funzionalità delle carte saranno definite nelle classi che implementano questa interfaccia.
 */

public interface ICard {
	/**
	 * Restituisce genericamente il nome delle carta
	 * Ad esempio, una carta territorio restituirà il territorio associato.
	 * 
	 * @return Nome delle carta, non può essere null
	 */
	String getName();
	
	/**
	 * Restituisce il simbolo associato alla carta se presente
	 * Ad esempio, una carta territorio avrà un simbolo che rappresente Infantry, Artillery, Cavalry
	 * @return il simbolo della carta, non può essere null
	 */
	ISymbolCard getSymbol();  
	
}
