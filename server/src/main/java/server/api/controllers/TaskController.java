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
import server.services.TaskService;

import java.util.Optional;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final SimpMessagingTemplate msgs;
    private TaskService taskService;

    /**
     * controller for the task route
     *
     * @param taskService service for task
     * @param msgs     object to send messages to connected websockets
     */
    public TaskController(TaskService taskService, SimpMessagingTemplate msgs) {
        this.taskService = taskService;
        this.msgs = msgs;

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
        Task editedTask = taskService.editTask(id, task);
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

        Card card = taskService.move(id, direction);
        msgs.convertAndSend("/topic/card", card);
    }


}
