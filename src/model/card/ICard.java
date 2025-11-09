package model.card;

import model.board.IZone;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, inclusi bonus, azioni speciali o rappresentare territori.
 * La struttura specifica e le funzionalità delle carte saranno definite nelle classi che implementano questa interfaccia.
 * La strutrura è in forma di JSON per facilitare la serializzazione e deserializzazione.
 */

//public interface ICard {
//	JsonObject toJson();
//}

public interface ICard {
	/**
	 * Restituisce genericamente il nome delle carta
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
