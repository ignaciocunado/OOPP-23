package server.api.controllers;

import commons.entities.Card;
import commons.entities.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TaskRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

import java.util.Optional;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final SimpMessagingTemplate msgs;
    private final CardRepository cardRepo;
    private final TaskRepository taskRepo;

    /**
     * controller for the task route
     *
     * @param cardRepo repository for cards
     * @param taskRepo repository for task
     * @param msgs     object to send messages to connected websockets
     */
    public TaskController(final CardRepository cardRepo, final TaskRepository taskRepo, SimpMessagingTemplate msgs) {
        this.msgs = msgs;
        this.taskRepo = taskRepo;
        this.cardRepo = cardRepo;
    }


    /**
     * endpoint for editing the name of a card list
     *
     * @param id     int value representing the id of a task
     * @param task   the task we are editing
     * @param errors wrapping object for potential validating errors
     * @return the task with the changed name
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Task> editTask(@PathVariable final int id,
                                         @Validated @RequestBody final Task task,
                                         final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if (!taskRepo.existsById(id)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        final Task editedTask = taskRepo.getById(id);
        editedTask.setName(task.getName());
        editedTask.setCompleted(task.isCompleted());
        taskRepo.save(editedTask);
        msgs.convertAndSend("/topic/task", editedTask);
        return new ResponseEntity<>(editedTask, new HttpHeaders(), 200);
    }

    /**
     * endpoint for changing the order of tasks
     *
     * @param id        integer representing the id of the task
     * @param direction the direction to move the tag in
     */
    @GetMapping("/{id}/move/{direction}")
    public void move(@PathVariable final Integer id,
                     @PathVariable final String direction) {

        final Optional<Card> cardOpt = this.cardRepo.findByTaskId(id);
        if (cardOpt.isEmpty()) {
            throw new EntityNotFoundException("No card with task id " + id);
        }
        final Card card = cardOpt.get();

        final Optional<Task> taskOpt = this.taskRepo.findById(id);
        if (taskOpt.isEmpty()) {
            throw new EntityNotFoundException("No task with id " + id);
        }
        final Task task = taskOpt.get();

        final int currentPosition = card.getNestedTaskList().indexOf(task);

        if (direction.equals("up")) {
            if (currentPosition == 0) return;

            final Task oldTask = card.getNestedTaskList().get(currentPosition-1);
            card.getNestedTaskList().set(currentPosition-1, task);
            card.getNestedTaskList().set(currentPosition, oldTask);
        }

        if (direction.equals("down")) {
            if (currentPosition == card.getNestedTaskList().size()-1) return;

            final Task oldTask = card.getNestedTaskList().get(currentPosition+1);
            card.getNestedTaskList().set(currentPosition+1, task);
            card.getNestedTaskList().set(currentPosition, oldTask);
        }

        msgs.convertAndSend("/topic/card", card);
        this.cardRepo.save(card);
    }


}
