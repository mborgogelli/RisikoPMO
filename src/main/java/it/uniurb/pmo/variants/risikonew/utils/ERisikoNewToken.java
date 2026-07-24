package it.uniurb.pmo.variants.risikonew.utils;

import it.uniurb.pmo.framework.players.ITokenType;

/**
 * Tipi di pedina disponibili nella variante RisikoNew.
 * Nella variante classica esiste un solo tipo: il carro armato (TANK).
 */
public enum ERisikoNewToken implements ITokenType {
    TANK("Tank", 1);

    private final String name;
    private final int value;

    ERisikoNewToken(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
