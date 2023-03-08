package server.api;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.springframework.data.repository.query.Param;
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


    public CardController(CardService cardService, Random random, CardRepository cardRepository) {
        this.cardService = cardService;
        this.random = random;
        this.cardRepository = cardRepository;
    }
    /**
     * to do
     * @param id to do
     * @return
     */
    @PatchMapping("/{id}")
    public int editCard(@PathVariable int id, @RequestBody Card card) {

    }

    /**
     * to do
     * @return to do
     */
    @PostMapping("/{id}/tag")
    public ResponseEntity<Tag> createTag(@PathVariable int id, @RequestBody Tag tag) {
        if(tag.getName() != null || tag.getColour() < 0){
            return ResponseEntity.badRequest().build();
        }
        cardRepository.findById(id).get().addTag(tag);

    }

    /**
     * to do
     * @return to do
     */
    @DeleteMapping("/{id}/tag")
    public int deleteTag(@PathVariable int id, ) {

    }

    /**
     * to do
     * @return to do
     */
    @PostMapping("/{id}/task")
    public ResponseEntity<Task> createTask(@PathVariable int id, @RequestBody Task task){
        if(task.getId() < 0 || task.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        cardRepository.findById(id).get().addTask(task);
    }

    /**
     * to do
     * @return to do
     */
    @DeleteMapping("/{id}/task")
    public int deleteTask(@PathVariable int id, ) {

    }

}
