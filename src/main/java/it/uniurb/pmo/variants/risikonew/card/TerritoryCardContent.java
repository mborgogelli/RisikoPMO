package it.uniurb.pmo.variants.risikonew.card;

public record TerritoryCardContent(ERisikoNewTerritorySymbols symbol, String territory) implements ITerritoryCardContent {
    @Override
    public ERisikoNewTerritorySymbols getCardSymbol() {
        return this.symbol;
    }

    @Override
    public String getCardTerriotyName() {
        return this.territory;
    }
}
