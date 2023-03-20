package server.api.controllers;

import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TagRepository;
import server.database.TaskRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;


@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardRepository cardRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    /**
     * Constructor
     *
     * @param cardRepository card DB
     * @param tagRepository  tag DB
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
     *
     * @param id   id of the Card to edit
     * @param card new Card to take the info from
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
     *
     * @param id  id of the Card in which to store the Tag
     * @param tag the Tag to create and add
     * @return ResponseEntity for status
     */
    @PostMapping("/{id}/tag")
    public ResponseEntity<Card> createTag(@PathVariable final int id,
                                          @Validated @RequestBody Tag tag,
                                          final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("No card with id " + id);
        }
        tagRepository.save(tag);

        Card containsNewTag = cardRepository.getById(id);
        containsNewTag.addTag(tag);
        return new ResponseEntity<>(cardRepository.save(containsNewTag), new HttpHeaders(),
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
        if (!tagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        Card deleteTagFrom = cardRepository.getById(id);
        if (!deleteTagFrom.removeTagById(tagId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tagRepository.deleteById(tagId);
        return new ResponseEntity<>(cardRepository.save(deleteTagFrom), new HttpHeaders(),
                200);
    }

    /**
     * creates a Task and stores it in a Card
     *
     * @param id   id of the Card in which to store the Task
     * @param task the Task to create and add
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
     *
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
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("No task with id " + id);
        }

        Card deleteTaskFrom = cardRepository.getById(id);
        if (!deleteTaskFrom.removeTaskById(taskId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        taskRepository.deleteById(taskId);
        return new ResponseEntity(cardRepository.save(deleteTaskFrom), new HttpHeaders(), 200);
    }
}
