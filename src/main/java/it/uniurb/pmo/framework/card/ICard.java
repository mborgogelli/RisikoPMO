package it.uniurb.pmo.framework.card;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, inclusi bonus, azioni speciali o rappresentare territori.
 * La struttura specifica e le funzionalità delle carte saranno definite nelle classi che implementano questa interfaccia.
 */

public interface ICard <T extends ICardType, TCardContent> {
	
	/**
	 * Restituisce il tipo della carta, che può essere utilizzato per identificare la categoria o la funzione della carta.
	 * Ad esempio, una carta territorio restituirà il simbolo del territorio, mentre una carta missione restituirà il simbolo della missione.
	 * 
	 * @return Il tipo della carta, non può essere null
	 */
	ICardType getCardType();  

	/**
	 * Restituisce il contenuto della carta, che può essere utilizzato per rappresentare informazioni specifiche legate alla carta.
	 * Ad esempio, una carta territorio potrebbe restituire il nome del territorio, mentre una carta missione potrebbe restituire i dettagli della missione.
	 * 
	 * @return Il contenuto della carta, non può essere null
	 */
	TCardContent getCardContent();
}
