package server.services;

import commons.entities.Card;
import commons.entities.CardList;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;
import server.database.CardRepository;
import server.exceptions.EntityNotFoundException;

import java.util.Optional;

@Service
public final class CardListService {
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    /**
     * Creates a card list service using the repository
     *
     * @param cardRepository     the repository for card data
     * @param cardListRepository the repository for card list data
     */
    public CardListService(final CardRepository cardRepository,
                           final CardListRepository cardListRepository) {
        this.cardRepository = cardRepository;
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
    /**
     * Creates a new Card in the specified CardList
     *
     * @param id The id of the CardList to add the new Card to
     * @param payload The Card data to be added
     * @return The updated CardList
     * @throws EntityNotFoundException if the specified CardList does not exist
     */
    public CardList createCard(final int id, final Card payload) {
        final Optional<CardList> cardListOpt = this.cardListRepository.findById(id);
        if(cardListOpt.isEmpty()) {
            throw new EntityNotFoundException("No card list with id " + id);
        }
        final Card card = new Card(payload.getTitle(), payload.getDescription());
        this.cardRepository.save(card);

        final CardList cardList = cardListOpt.get();
        cardList.addCard(card);
        this.cardListRepository.save(cardList);
        return cardList;
    }
    /**
     * Deletes a Card from the specified CardList
     *
     * @param id The id of the CardList to delete the Card from
     * @param cardId The id of the Card to be deleted
     * @return The updated CardList
     * @throws EntityNotFoundException if the specified CardList or Card does not exist
     */
    public CardList deleteCard(final int id, final int cardId) {
        final Optional<CardList> cardListOpt = this.cardListRepository.findById(id);
        if (cardListOpt.isEmpty()) {
            throw new EntityNotFoundException("No card list with id " + id);
        }

        final CardList cardList = cardListOpt.get();
        if(!cardList.removeCardById(cardId)) {
            throw new EntityNotFoundException("Card list contains no card with id " + cardId);
        }

        this.cardRepository.deleteById(cardId);
        cardListRepository.save(cardList);
        return cardList;
    }
    /**
     * Edits the title of a CardList
     *
     * @param id The id of the CardList to be edited
     * @param cardList The updated CardList with the new title
     * @return The updated CardList
     * @throws EntityNotFoundException if the specified CardList does not exist
     */
    public CardList editCardList(final int id, final CardList cardList) {
        final Optional<CardList> cardListOpt = this.cardListRepository.findById(id);
        if(cardListOpt.isEmpty()) {
            throw new EntityNotFoundException("No card list with id " + id);
        }

        final CardList editedCardList = cardListOpt.get();
        editedCardList.setTitle(cardList.getTitle());
        editedCardList.setColour(cardList.getColour());
        editedCardList.setTextColour(cardList.getTextColour());
        cardListRepository.save(editedCardList);
        return editedCardList;
    }
}
