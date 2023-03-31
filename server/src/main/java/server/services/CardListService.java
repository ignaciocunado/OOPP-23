package server.services;

import commons.entities.CardList;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

@Service
public final class CardListService {
    private final CardListRepository cardListRepository;


    /**
     * Creates a card list service using the repository
     *
     * @param cardListRepository the repository for card list data
     */
    public CardListService(final CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    /**
     * Saves the specified CardList to the database.
     *
     * @param cardList the CardList to be saved
     * @return the saved CardList
     */
    public CardList createCardList(final CardList cardList) {
        return this.cardListRepository.save(cardList);
    }

}
