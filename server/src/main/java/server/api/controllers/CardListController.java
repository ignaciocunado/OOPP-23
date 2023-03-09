package server.api.controllers;

import commons.Card;
import commons.CardList;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.server.ResponseStatusException;
import server.database.CardRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;

@RestController
@RequestMapping("/api/list")

public class CardListController {

    private final CardListRepository cardListRepo;
    private final CardRepository cardRepo;

    /** Controller for the CardList route
     * @param cardlistRepo repository for card list
     * @param cardRepo repository for card
     */
    public CardListController(final CardListRepository cardlistRepo,
                              final CardRepository cardRepo) {
        this.cardListRepo = cardlistRepo;
        this.cardRepo = cardRepo;
    }

    /** create a card into a card list
     * @param id of the card list in which the card gets created
     * @return the card list containing the new card
     */
    @PostMapping("/{id}/card")
    public ResponseEntity<CardList> createCard(@PathVariable final Integer id) {
        try {
            final CardList cardlist = this.cardListRepo.getById(id);
            final Card card = new Card("New Card", "New Description");
            this.cardRepo.save(card);

            cardlist.addCard(card);
            this.cardListRepo.save(cardlist);
            return new ResponseEntity<>(this.cardListRepo.getById(id), new HttpHeaders(), 200);
        } catch (final JpaObjectRetrievalFailureException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Card list not found", e);
        }
    }

    /**
     * @param id integer representing the id of the card list
     * @param cardId integer representing the id of the card we are deleting
     * @return the card list without the card
     */
    @DeleteMapping("/{id}/card/{cardid}")
    public ResponseEntity<CardList> deleteCard(@PathVariable final Integer id,
                                               @PathVariable final Integer cardId) {
        try {
            final CardList cardList = this.cardListRepo.getById(id);
            if(!cardList.removeCardById(cardId)){
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Card not found");
            }

            this.cardRepo.deleteById(cardId);
            return new ResponseEntity<>(cardList, new HttpHeaders(), 200);
        } catch (final JpaObjectRetrievalFailureException e ) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Card list not found", e);
        }
    }

    /** endpoint for editing the title of a card list
     * @param id int value representing the id of a card list
     * @param newTitle the new title assigned to the card list
     * @return the card list with the changed new title
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CardList> editCardListTitle(@PathVariable final int id,
                                                            @RequestBody String newTitle) {
        if(!cardListRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        CardList editedCardList = cardListRepo.getById(id);
        editedCardList.setTitle(newTitle);
        cardListRepo.save(editedCardList);
        return new ResponseEntity<>(cardListRepo.getById(id), new HttpHeaders(), 200);
    }
}


