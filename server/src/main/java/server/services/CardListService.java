package server.services;

import commons.entities.Card;
import commons.entities.Tag;
import org.springframework.stereotype.Service;
import server.database.CardRepository;
import server.database.TagRepository;

import java.util.List;

@Service
public final class CardService {
    private final CardRepository cardRepository;

    public CardService(final CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card createCard(final Card card) {
        return this.cardRepository.save(card);
    }

}
