package it.uniurb.pmo.framework.card;

import java.util.List;

public interface ICardCreator<TCardType extends ICardType, TCardContent> {

    List<ICard<TCardType, TCardContent>> getDeck();
}
