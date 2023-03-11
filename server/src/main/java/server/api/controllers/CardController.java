package server.api.controllers;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TagRepository;
import server.database.TaskRepository;


@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardRepository cardRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    /**
     * Constructor
     * @param cardRepository card DB
     * @param tagRepository tag DB
     * @param taskRepository task DB
     */
    public CardController(final CardRepository cardRepository, final TagRepository tagRepository,
                          final TaskRepository taskRepository) {
        this.cardRepository = cardRepository;
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Edits a Card
     * @param id id of the Card to edit
     * @param card new Card to take the info from
     * @return ResponseEntity for status
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Card> editCard(@PathVariable final int id, @RequestBody Card card) {
        if(!cardRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Card toEdit = cardRepository.getById(id);
        toEdit.setTitle(card.getTitle());
        toEdit.setDescription(card.getDescription());
        return new ResponseEntity<>(cardRepository.save(toEdit), new HttpHeaders(), 200);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Card containsNewTag = cardRepository.getById(id);
        containsNewTag.addTag(tag);
        tagRepository.save(tag);
        return new ResponseEntity<>(cardRepository.save(containsNewTag), new HttpHeaders(),
            200);
    }

    /**
     * deletes a Tag iff it exists
     * @param id id of the Card
     * @param tagId id of the Tag
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/tag/{tagId}")
    public ResponseEntity<Card> deleteTag(@PathVariable final int id,
                                          @PathVariable final int tagId) {
        if(!cardRepository.existsById(id) || !tagRepository.existsById(tagId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Card deleteTagFrom = cardRepository.getById(id);
        Tag tagToBeDeleted = tagRepository.getById(tagId);
        boolean deleted = deleteTagFrom.removeTag(tagToBeDeleted);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tagRepository.deleteById(tagId);
        return new ResponseEntity<>(cardRepository.save(deleteTagFrom), new HttpHeaders(),
            200);
    }

    /**
     * creates a Task and stores it in a Card
     * @param id id of the Card in which to store the Task
     * @param task the Task to create and add
     * @return ResponseEntity for status
     */
    @PostMapping("/{id}/task")
    public ResponseEntity<Card> createTask(@PathVariable final int id, @RequestBody Task task){
        if(task == null || task.getId() < 0 || task.getName() == null || id < 0 ||
            !cardRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Card containsTask = cardRepository.getById(id);
        containsTask.addTask(task);
        taskRepository.save(task);
        return new ResponseEntity<>(cardRepository.save(containsTask), new HttpHeaders(),
            200);
    }

    /**
     * Deletes a Task iff it exists
     * @param id id of the Card
     * @param taskId id of the Task
     * @return ResponseEntity for status
     */
    @DeleteMapping("/{id}/task/{taskId}")
    public ResponseEntity<Card> deleteTask(@PathVariable final int id,
                                           @PathVariable final int taskId) {
        if(!cardRepository.existsById(id) || !taskRepository.existsById(taskId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Card deleteTaskFrom = cardRepository.getById(id);
        Task taskToBeDeleted = taskRepository.getById(taskId);
        boolean deleted = deleteTaskFrom.removeTask(taskToBeDeleted);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        taskRepository.deleteById(taskId);
        return new ResponseEntity(cardRepository.save(deleteTaskFrom), new HttpHeaders(), 200);
    }
}
