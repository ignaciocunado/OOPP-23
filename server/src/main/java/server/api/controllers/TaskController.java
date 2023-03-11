package server.api.controllers;

import commons.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;

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
    public ResponseEntity<Task> editTaskNameAndBoolean(@PathVariable final int id,
                                             @RequestBody final Task task) {
        if(!taskRepo.existsById(id)) {

            return ResponseEntity.notFound().build();

        }

        final Task editedTask = taskRepo.getById(id);
        editedTask.setName(task.getName());
        editedTask.setCompleted(task.isCompleted());
        taskRepo.save(editedTask);
        return new ResponseEntity<>(taskRepo.getById(id), new HttpHeaders(), 200);
    }


}
