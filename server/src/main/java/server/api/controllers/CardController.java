package server.api.controllers;

import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.*;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

import java.util.Optional;


@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardListRepository cardListRepository;
    private final CardRepository cardRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    /**
     * Constructor
     *
     * @param cardListRepository cardList DB
     * @param cardRepository     card DB
     * @param tagRepository      tag DB
     * @param taskRepository     task DB
     */
    public CardController(final CardListRepository cardListRepository,
                          final CardRepository cardRepository,
                          final TagRepository tagRepository,
                          final TaskRepository taskRepository) {
        this.cardListRepository = cardListRepository;
        this.cardRepository = cardRepository;
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Edits a Card
     *
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
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }

        Card toEdit = cardRepository.getById(id);
        toEdit.setTitle(card.getTitle());
        toEdit.setDescription(card.getDescription());
        return new ResponseEntity<>(cardRepository.save(toEdit), new HttpHeaders(), 200);
    }


    /**
     * creates a Tag and stores it in a Card
     * @param id id of the Card in which to store the Tag
     * @param tagId the id of the tag that is assigned to the board and card
     * @return ResponseEntity for status
     */
    @PostMapping("/{id}/tag/{tagId}")
    public ResponseEntity<Card> assignTag(@PathVariable final int id,
                                          @PathVariable final int tagId) {

        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        Card card = cardRepository.getById(id);
        card.addTag(tagRepository.getById(tagId));
        return new ResponseEntity<>(cardRepository.save(card), new HttpHeaders(),
            200);
    }

    /**
     * deletes a Tag iff it exists
     *
     * @param id    id of the Card
     * @param tagId id of the Tag
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/tag/{tagId}")
    public ResponseEntity<Card> deleteTag(@PathVariable final int id,
                                          @PathVariable final int tagId) {
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        Card deleteTagFrom = cardRepository.getById(id);
        if (!deleteTagFrom.removeTagById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        return new ResponseEntity<>(cardRepository.save(deleteTagFrom), new HttpHeaders(),
                200);
    }

    /**
     * creates a Task and stores it in a Card
     *
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
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }

        taskRepository.save(task);

        Card containsTask = cardRepository.getById(id);
        containsTask.addTask(task);
        return new ResponseEntity<>(cardRepository.save(containsTask), new HttpHeaders(),
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
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        Card deleteTaskFrom = cardRepository.getById(id);
        if (!deleteTaskFrom.removeTaskById(taskId)) {
            throw new EntityNotFoundException("No task with id in this card " + id);
        }

        taskRepository.deleteById(taskId);
        return new ResponseEntity<>(cardRepository.save(deleteTaskFrom), new HttpHeaders(), 200);
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
                      @PathVariable final Integer position) {
        if(!this.cardRepository.existsById((id))){
            throw new EntityNotFoundException("No card with id " + id);
        }
        final Card card = this.cardRepository.getById(id);
        final Optional<CardList> srcOpt = this.cardListRepository.findByCardId(id);
        final Optional<CardList> destOpt = this.cardListRepository.findById(listId);

        if (srcOpt.isEmpty()) {
            throw new EntityNotFoundException("No card list associated with card id " + id);
        }

        if(destOpt.isEmpty()){
            throw new EntityNotFoundException("No card list with id " + listId);
        }

        srcOpt.get().removeCard(card);
        // Rounded to closest and can be maximum the amount of children already there.
        destOpt.get().getCards().add(Math.min(destOpt.get().getCards().size(), position), card);
        this.cardListRepository.save(srcOpt.get());
        this.cardListRepository.save(destOpt.get());
    }


}
