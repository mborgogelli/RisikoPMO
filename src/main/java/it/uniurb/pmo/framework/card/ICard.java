package it.uniurb.pmo.framework.card;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, inclusi bonus, azioni speciali o rappresentare territori.
 * La struttura specifica e le funzionalità delle carte saranno definite nelle classi che implementano questa interfaccia.
 */

public interface ICard <ICardType, T> {
	/**
	 * Restituisce genericamente il nome delle carta
	 * Ad esempio, una carta territorio restituirà il territorio associato.
	 * 
	 * @return Nome delle carta, non può essere null
	 */
	String getName();
	
	/**
	 * Restituisce il tipo della carta, che può essere utilizzato per identificare la categoria o la funzione della carta.
	 * Ad esempio, una carta territorio restituirà il simbolo del territorio, mentre una carta missione restituirà il simbolo della missione.
	 * 
	 * @return Il tipo della carta, non può essere null
	 */
	ICardType getCardType();  
	
	
}
