package server.services;

import commons.entities.Card;
import commons.entities.CardList;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;
import server.database.CardRepository;

@Service
public final class CardListService {
    private final CardListRepository cardListRepository;

    public CardListService(final CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    public CardList createCardList(final CardList cardList) {
        return this.cardListRepository.save(cardList);
    }

}
