package it.uniurb.pmo.framework.card;

import java.util.List;

public interface ICardCreator<ICardType, TCardContent> {

    List<ICard<ICardType, TCardContent>> getDeck();
}
