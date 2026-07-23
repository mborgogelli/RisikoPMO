package it.uniurb.pmo.variants.risikonew;

import it.uniurb.pmo.framework.players.ITokenType;

/**
 * Tipi di pedina disponibili nella variante RisikoNew.
 * Nella variante classica esiste un solo tipo: il carro armato (TANK).
 */
public enum RisikoToken implements ITokenType {
    TANK;

    @Override
    public String getName() {
        return this.name();
    }
}
