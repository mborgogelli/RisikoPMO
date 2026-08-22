package it.uniurb.pmo.framework.card;

/**
 * Rappresenta una carta generica che può essere utilizzata in qualunque variante di Risiko.
 */

public interface ICard {
	
	/**
	 * Restituisce il tipo della carta, che può essere utilizzato per identificare la categoria della carta.
	 * Ad esempio, una carta territorio restituirà il simbolo del territorio, mentre una carta missione restituirà il tipo della missione.
	 * 
	 * @return Il tipo della carta
	 */
	ICardType getCardType();

	/**
	 * Restituisce il contenuto della carta, che può essere utilizzato per rappresentare informazioni specifiche legate alla carta.
	 * Ad esempio, una carta territorio potrebbe restituire il nome del territorio, mentre una carta missione potrebbe restituire i dettagli della missione.
	 * 
	 * @return Il contenuto della carta
	 */
	ICardContent getCardContent();
	
}
