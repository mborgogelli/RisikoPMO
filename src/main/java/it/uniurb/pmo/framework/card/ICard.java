package it.uniurb.pmo.framework.card;

/**
 * Rappresenta una carta generica che può essere utilizzata in qualunque variante di Risiko.
 *
 * @param <TCardType> Il tipo della carta, che deve estendere ICardType. Questo permette di identificare la categoria o la funzione della carta.
 * @param <TCardContent> Il contenuto della carta, che rappresenta informazioni specifiche legate alla carta. Ad esempio,
 *                       potrebbe essere il nome di un territorio o i dettagli di una missione.
 */

public interface ICard <TCardType extends ICardType, TCardContent> {
	
	/**
	 * Restituisce il tipo della carta, che può essere utilizzato per identificare la categoria della carta.
	 * Ad esempio, una carta territorio restituirà il simbolo del territorio, mentre una carta missione restituirà il tipo della missione.
	 * 
	 * @return Il tipo della carta
	 */
	TCardType getCardType();

	/**
	 * Restituisce il contenuto della carta, che può essere utilizzato per rappresentare informazioni specifiche legate alla carta.
	 * Ad esempio, una carta territorio potrebbe restituire il nome del territorio, mentre una carta missione potrebbe restituire i dettagli della missione.
	 * 
	 * @return Il contenuto della carta
	 */
	TCardContent getCardContent();
}
