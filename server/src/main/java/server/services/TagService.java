package server.services;

import commons.entities.Card;
import commons.entities.Tag;
import org.springframework.stereotype.Service;
import server.database.CardRepository;
import server.database.TagRepository;

import java.util.List;

@Service
public final class TagService {

    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    /**
     * Creates a tag service using the repositories provided
     *
     * @param tagRepository the repository for tag data
     * @param cardRepository the repository for card data
     */
    public TagService(final TagRepository tagRepository,
                      final CardRepository cardRepository) {
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
    }

    /**
     * Saves the specified Tag to the database.
     *
     * @param tag the tag to save
     * @return the saved tag
     */
    public Tag createTag(final Tag tag) {
        return this.tagRepository.save(tag);
    }

    /**
     * Deletes the tag from all cards
     *
     * @param id the tag to be deleted
     */
    public void deleteTagFromCards(final int id) {
        final List<Integer> cardIds = cardRepository.selectCardsWithTag(id);
        cardIds.forEach(cardId -> {
            Card card = this.cardRepository.getById(cardId);
            card.removeTagById(id);
            this.cardRepository.save(card);
        });
    }

}
