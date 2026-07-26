package it.uniurb.pmo.framework.turn;

import java.util.Map;

public interface IMessageToPlayer <X,Y> {

    String getMessage();

    Map<X,Y> getContent();
}
