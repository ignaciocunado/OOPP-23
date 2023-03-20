package server.api.controllers;

import commons.entities.Card;
import commons.entities.CardList;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import server.database.CardRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/list")

public class CardListController {

    private final CardListRepository cardListRepo;
    private final CardRepository cardRepo;

    /** Controller for the CardList route
     * @param cardListRepo repository for card list
     * @param cardRepo repository for card
     */
    public CardListController(final CardListRepository cardListRepo,
                              final CardRepository cardRepo) {
        this.cardListRepo = cardListRepo;
        this.cardRepo = cardRepo;
    }

    /**
     * endpoint for creating a card into a list
     *
     * @param id of the card list in which the card gets created
     * @param payload the data for the new card
     * @return the card list containing the new card
     */
    @PostMapping("/{id}/card")
    public ResponseEntity<CardList> createCard(@PathVariable final Integer id,
                                               @Validated @RequestBody final Card payload,
                                               final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if(!this.cardListRepo.existsById(id)){
            throw new EntityNotFoundException("No card list with id " + id);
        }

        final Card card = new Card(payload.getTitle(), payload.getDescription());
        this.cardRepo.save(card);

        final CardList cardList = this.cardListRepo.getById(id);
        cardList.addCard(card);
        return new ResponseEntity<>(this.cardListRepo.save(cardList), new HttpHeaders(), 200);

    }

    /** endpoint for deleting a card by it's id
     * @param id integer representing the id of the card list
     * @param cardId integer representing the id of the card we are deleting
     * @return the card list without the card
     */
    @DeleteMapping("/{id}/card/{cardId}")
    public ResponseEntity<CardList> deleteCard(@PathVariable final Integer id,
                                               @PathVariable final Integer cardId) {
        if(!this.cardListRepo.existsById((id))){
            throw new EntityNotFoundException("No card list with id " + id);
        }

        final CardList cardList = this.cardListRepo.getById(id);
        if(!cardList.removeCardById(cardId)) {
            throw new EntityNotFoundException("Card list contains no card with id " + cardId);
        }

        this.cardRepo.deleteById(cardId);
        return new ResponseEntity<>(cardList, new HttpHeaders(), 200);
    }

    /** endpoint for editing the title of a card list
     * @param id int value representing the id of a card list
     * @param cardList the card list edited
     * @return the card list with the changed new title
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CardList> editCardListTitle(@PathVariable final int id,
                                                      @Validated @RequestBody final CardList cardList,
                                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if(!cardListRepo.existsById(id)) {
            throw new EntityNotFoundException("No card list with id " + id);
        }

        final CardList editedCardList = this.cardListRepo.getById(id);
        editedCardList.setTitle(cardList.getTitle());
        return new ResponseEntity<>(cardListRepo.save(editedCardList), new HttpHeaders(), 200);
    }
}
