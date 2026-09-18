package it.uniurb.pmo.framework.turn.command;

import java.util.Map;

public record DeployCommand(Map<String, Integer> deployment) implements IGameCommand  {

    public DeployCommand {
        deployment = Map.copyOf(deployment);
    }
}
