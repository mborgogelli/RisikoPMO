package it.uniurb.pmo.framework.card;

import java.util.List;

/**
* Classe astratta che offre il comportamento di base per costruire le carte del gioco
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura delle carte, lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* delle carte in base alle regole della variante di Risiko implementata.
*/

public abstract class CardCreator implements ICardCreator {

    /**
     * Metodo astratto: deve essere implementato per creare il mazzo di carte specifico
     * @return List<ICard<T, TCardContent>> Il mazzo di carte del gioco
     */
    protected abstract List<ICard> createDeck(ICardType cardType);

    /**
     * Metodo che restituisce il mazzo di carte del gioco.
     * @return List<ICard<T, TCardContent>> Il mazzo di carte del gioco
     */
    public List<ICard> getDeck(ICardType cardType) {
		return createDeck(cardType);
	}
}