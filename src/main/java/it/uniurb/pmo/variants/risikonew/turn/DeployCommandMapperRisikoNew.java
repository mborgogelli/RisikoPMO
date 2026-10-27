package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.turn.command.IDeployCommand;
import it.uniurb.pmo.variants.risikonew.turn.command.DeployCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Arrays;
import java.util.Map;

/** Converte i dati generici del controller nel comando di deploy di RisikoNew. */
public class DeployCommandMapperRisikoNew {

    public IDeployCommand<ERisikoNewToken> toCommand(String tokenName, Map<String, Integer> deployment) {
        ERisikoNewToken tokenType = this.toTokenType(tokenName);
        return new DeployCommandRisikoNew(tokenType, deployment);
    }

    public ERisikoNewToken toTokenType(String tokenName) {
        return Arrays.stream(ERisikoNewToken.values())
                .filter(token -> token.name().equals(tokenName) || token.getName().equals(tokenName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown token type: " + tokenName));
    }
}
