package server.api.controllers;

import commons.entities.Card;
import commons.entities.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.exceptions.InvalidRequestException;
import server.services.CardService;


@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;
    private final SimpMessagingTemplate msgs;

    /** Constructor for the cardService
     * @param cardService the service for the bussiness logic
     * @param msgs object to send messages to connected websockets
     */
    public CardController(CardService cardService, SimpMessagingTemplate msgs) {
        this.cardService = cardService;
        this.msgs = msgs;
    }


    /**
     * Edits a Card
     * @param id   id of the Card to edit
     * @param card new Card to take the info from
     * @param errors wrapping object for potential validating errors
     * @return ResponseEntity for status
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Card> editCard(@PathVariable final int id,
                                         @Validated @RequestBody Card card,
                                         final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        Card toEdit = cardService.editCard(id, card);
        msgs.convertAndSend("/topic/card", toEdit);
        return new ResponseEntity<>(toEdit, new HttpHeaders(), 200);
    }


    /**
     * creates a Tag and stores it in a Card
     * @param id id of the Card in which to store the Tag
     * @param tagId the id of the tag that is assigned to the board and card
     * @return ResponseEntity for status
     */
    @PutMapping("/{id}/tag/{tagId}")
    public ResponseEntity<Card> assignTag(@PathVariable final int id,
                                          @PathVariable final int tagId) {

        Card card = cardService.assignTag(id, tagId);
        msgs.convertAndSend("/topic/card", card);
        return new ResponseEntity<>(card, new HttpHeaders(),
            200);
    }

    /**
     * deletes a Tag iff it exists
     * @param id    id of the Card
     * @param tagId id of the Tag
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/tag/{tagId}")
    public ResponseEntity<Card> deleteTag(@PathVariable final int id,
                                          @PathVariable final int tagId) {
        Card deleteTagFrom = cardService.deleteTag(id, tagId);
        msgs.convertAndSend("/topic/card", deleteTagFrom);
        return new ResponseEntity<>(deleteTagFrom, new HttpHeaders(),
                200);
    }

    /**
     * creates a Task and stores it in a Card
     * @param id   id of the Card in which to store the Task
     * @param task the Task to create and add
     * @param errors wrapping object for potential validating errors
     * @return ResponseEntity for status
     */
    @PostMapping("/{id}/task")
    public ResponseEntity<Card> createTask(@PathVariable final int id,
                                           @Validated @RequestBody Task task,
                                           final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        Card containsTask = cardService.createTask(id, task);
        msgs.convertAndSend("/topic/card", containsTask);
        return new ResponseEntity<>(containsTask, new HttpHeaders(),
                200);
    }

    /**
     * Deletes a Task iff it exists
     * @param id     id of the Card
     * @param taskId id of the Task
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/task/{taskId}")
    public ResponseEntity<Card> deleteTask(@PathVariable final int id,
                                           @PathVariable final int taskId) {
        Card deleteTaskFrom = cardService.deleteTask(id, taskId);
        msgs.convertAndSend("/topic/card", deleteTaskFrom);
        return new ResponseEntity<>(deleteTaskFrom, new HttpHeaders(), 200);
    }

    /**
     * endpoint for changing the list to which a card is assigned to based on its id
     * @param id integer representing the id of the card
     * @param listId integer representing the list to which the card will be added
     * @param position the position in the children of the list to which the card will be added
     */
    @GetMapping("/{id}/move/{listId}/{position}")
    public void move(@PathVariable final Integer id,
                     @PathVariable final Integer listId,
                     @PathVariable Integer position) {
        var pair = cardService.move(id,listId,position);
        msgs.convertAndSend("/topic/cardlist", pair.getKey());
        msgs.convertAndSend("/topic/cardlist", pair.getValue());
    }


}
