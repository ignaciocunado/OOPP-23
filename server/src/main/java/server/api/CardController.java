package server.api;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import java.util.Random;

@RestController
@RequestMapping("/card")
public class CardController {

    private CardService cardService;
    private final Random random;
    private final CardRepository cardRepository;

    /**
     * Constructor for card controller
     * @param cardService service for application logic
     * @param random injected object of random class
     * @param cardRepository repository of cards
     */
    public CardController(CardService cardService, Random random, CardRepository cardRepository) {
        this.cardService = cardService;
        this.random = random;
        this.cardRepository = cardRepository;
    }

    /**
     * to do
     * @param id to do
     * @param card to do
     * @return to do
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Card> editCard(@PathVariable final int id, @RequestBody Card card) {
        if(!cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

    }

    /**
     * creates a Tag and stores it in a Card
     * @param id id of the Card in which to store the Tag
     * @param tag the Tag to create and add
     * @return ResponseEntity for status
     */
    @PostMapping("/{id}/tag")
    public ResponseEntity<Card> createTag(@PathVariable final int id, @RequestBody Tag tag) {
        if(tag.getName() == null || tag.getColour() < 0 || id < 0 ||
            !cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card containsNewTag = cardRepository.getById(id);
        containsNewTag.addTag(tag);
        cardRepository.save(containsNewTag);
        return new ResponseEntity(cardRepository.getById(id), new HttpHeaders(), 200);

    }

    /**
     * deletes a Tag if it exists
     * @param id id of the Card where the Tag to be deleted is
     * @param tag the Tag to delete
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/tag")
    public ResponseEntity<Card> deleteTag(@PathVariable int id, @RequestBody Tag tag) {
        if(!cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card deleteTagFrom = cardRepository.getById(id);
        boolean deleted = deleteTagFrom.removeTag(tag);
        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }
        cardRepository.save(deleteTagFrom);
        return new ResponseEntity(cardRepository.getById(id), new HttpHeaders(), 200);
    }

    /**
     * creates a Task and stores it in a Card
     * @param id id of the Card in which to store the Task
     * @param task the Task to create and add
     * @return ResponseEntity for status
     */
    @PostMapping("/{id}/task")
    public ResponseEntity<Task> createTask(@PathVariable final int id, @RequestBody Task task){
        if(task.getId() < 0 || task.getName() == null || id < 0 || cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card containsTask = cardRepository.getById(id);
        containsTask.addTask(task);
        cardRepository.save(containsTask);
        return new ResponseEntity(cardRepository.getById(id), new HttpHeaders(), 200);
    }

    /**
     * deletes a Task if it exists
     * @param id id of the Card where the Task to be deleted is
     * @param task the Task to delete
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/task")
    public ResponseEntity<Card> deleteTask(@PathVariable final int id, @RequestBody Task task) {
        if(!cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Card deleteTaskFrom = cardRepository.getById(id);
        boolean deleted = deleteTaskFrom.removeTask(task);
        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }
        cardRepository.save(deleteTaskFrom);
        return new ResponseEntity(cardRepository.getById(id), new HttpHeaders(), 200);
    }
}
