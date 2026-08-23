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

	
}
