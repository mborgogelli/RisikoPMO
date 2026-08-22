package it.uniurb.pmo.variants.risikonew.card;

import it.uniurb.pmo.framework.card.ICardType;

/**
 * Chiave di partizione del mazzo.
 * Questo enum NON descrive il tipo della singola carta, ma categorizza il mazzo in macro-gruppi:
 * MISSION per le carte missione e TERRITORY per le carte territorio.
 * Il manager usa questi valori come chiavi.
 */
public enum ERisikoNewCardType implements ICardType {

    MISSION,
    TERRITORY,
}
