package it.uniurb.pmo.variants.risikonew.turn.command;

import it.uniurb.pmo.framework.turn.command.IDeployCommand;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Map;

// TODO possiamo metterla nel framework?
public record DeployCommandRisikoNew(ERisikoNewToken tokenType, Map<String, Integer> deployment)
        implements IDeployCommand<ERisikoNewToken> {

    public DeployCommandRisikoNew {
        deployment = Map.copyOf(deployment);
    }
}
