package server.api.controllers;

import commons.entities.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskRepository taskRepo;

    /** controller for the task route
     * @param taskRepo repository for task
     */
    public TaskController(final TaskRepository taskRepo){
        this.taskRepo = taskRepo;
    }


    /** endpoint for editing the name of a card list
     * @param id int value representing the id of a task
     * @param task the task we are editing
     * @return the task with the changed name
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Task> editTask(@PathVariable final int id,
                                         @Validated @RequestBody final Task task,
                                         final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if(!taskRepo.existsById(id)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        final Task editedTask = taskRepo.getById(id);
        editedTask.setName(task.getName());
        editedTask.setCompleted(task.isCompleted());
        return new ResponseEntity<>(taskRepo.save(editedTask), new HttpHeaders(), 200);
    }


}
