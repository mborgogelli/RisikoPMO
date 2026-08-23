package it.uniurb.pmo.variants.risikonew.card;

public record TerritoryCardContent(ERisikoNewTerritorySymbols symbol, String territory) implements ITerritoryCardContent {

    @Override
    public ERisikoNewTerritorySymbols getSymbol() {
        return this.symbol;
    }

    @Override
    public String getTerritoryName() {
        return this.territory;
    }
}
