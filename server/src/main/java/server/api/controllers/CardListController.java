package server.api.controllers;

import commons.Card;
import commons.CardList;
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

    /** endpoint for creating a card into a list
     * @param id of the card list in which the card gets created
     * @return the card list containing the new card
     */
    @PostMapping("/{id}/card")
    public ResponseEntity<CardList> createCard(@PathVariable final Integer id, @RequestBody final Card payload) {
        if(!this.cardListRepo.existsById(id)){
            return ResponseEntity.notFound().build();
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
            return ResponseEntity.notFound().build();
        }

        final CardList cardList = this.cardListRepo.getById(id);
        if(!cardList.removeCardById(cardId)) {
            return ResponseEntity.notFound().build();
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
                                                            @RequestBody final CardList cardList) {
        if(!cardListRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        final CardList editedCardList = this.cardListRepo.getById(id);
        editedCardList.setTitle(cardList.getTitle());
        return new ResponseEntity<>(cardListRepo.save(editedCardList), new HttpHeaders(), 200);
    }
}


