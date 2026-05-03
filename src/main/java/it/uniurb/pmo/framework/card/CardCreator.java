package it.uniurb.pmo.framework.card;

import java.util.List;

/**
* Classe astratta che offre il comportamento di base per costruire le carte del gioco
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura delle carte, lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* delle carte in base alle regole della variante di Risiko implementata.
*/

public abstract class CardCreator<T extends ICardType, TCardContent> implements ICardCreator<T, TCardContent> {

    /**
     * Metodo astratto: deve essere implementato per creare il mazzo di carte specifico
     * @return
     */
    protected abstract List<ICard<T, TCardContent>> createDeck();

    /**
     * Metodo che restituisce il mazzo di carte del gioco.
     * @return
     */
    public List<ICard<T, TCardContent>> getDeck() {
		return createDeck();
	}
}