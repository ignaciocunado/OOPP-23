package server.api.controllers;

import commons.entities.Card;
import commons.entities.CardList;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.exceptions.InvalidRequestException;
import server.services.CardListService;

@RestController
@RequestMapping("/api/list")

public class CardListController {

    private final CardListService cardListService;
    private final SimpMessagingTemplate msgs;

    /** Controller for the CardList route
     *
     * @param cardListService the service with the main business logic
     * @param msgs          object to send messages to connected websockets
     */
    public CardListController(final CardListService cardListService,
                              final SimpMessagingTemplate msgs) {
        this.cardListService = cardListService;
        this.msgs = msgs;
    }
    /**
     * endpoint for creating a card into a list
     *
     * @param id of the card list in which the card gets created
     * @param payload the data for the new card
     * @param errors wrapping object for potential validating errors
     * @return the card list containing the new card
     */
    @PostMapping("/{id}/card")
    public ResponseEntity<CardList> createCard(@PathVariable final Integer id,
                                               @Validated @RequestBody final Card payload,
                                               final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        CardList cardList = cardListService.createCard(id, payload);
        msgs.convertAndSend("/topic/cardlist", cardList);
        return new ResponseEntity<>(cardList, new HttpHeaders(), 200);

    }

    /** endpoint for deleting a card by its id
     * @param id integer representing the id of the card list
     * @param cardId integer representing the id of the card we are deleting
     * @return the card list without the card
     */
    @DeleteMapping("/{id}/card/{cardId}")
    public ResponseEntity<CardList> deleteCard(@PathVariable final Integer id,
                                               @PathVariable final Integer cardId) {
        CardList cardList = cardListService.deleteCard(id, cardId);
        msgs.convertAndSend("/topic/cardlist", cardList);
        return new ResponseEntity<>(cardList, new HttpHeaders(), 200);
    }

    /** endpoint for editing the title of a card list
     * @param id int value representing the id of a card list
     * @param cardList the card list edited
     * @param errors wrapping object for potential validating errors
     * @return the card list with the changed new title
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CardList> editCardList(@PathVariable final int id,
                                                      @Validated @RequestBody
                                                      final CardList cardList,
                                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        CardList editedCardList = cardListService.editCardList(id, cardList);
        msgs.convertAndSend("/topic/cardlist", editedCardList);
        return new ResponseEntity<>(editedCardList, new HttpHeaders(), 200);
    }
}
