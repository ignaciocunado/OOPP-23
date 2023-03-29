package server.services;

import commons.entities.Board;
import commons.entities.Card;
import commons.entities.Tag;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.TagRepository;
import server.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public final class TagService {

    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    public TagService(final TagRepository tagRepository,
                      final CardRepository cardRepository) {
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
    }

    public Tag createTag(final Tag tag) {
        return this.tagRepository.save(tag);
    }

    public void deleteTagFromCards(final int id) {
        final List<Integer> cardIds = cardRepository.selectCardsWithTag(id);
        cardIds.forEach(cardId -> {
            Card card = this.cardRepository.getById(cardId);
            card.removeTagById(id);
            this.cardRepository.save(card);
        });
    }

}
