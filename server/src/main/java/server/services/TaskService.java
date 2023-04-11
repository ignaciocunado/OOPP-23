package server.services;

import commons.entities.Task;
import org.springframework.stereotype.Service;
import server.database.TaskRepository;
import server.exceptions.EntityNotFoundException;

@Service
public class TaskService {
    private final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }
    public Task editTask(int id, Task  task){
        if(!taskRepo.existsById(id)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        final Task editedTask = taskRepo.getById(id);
        editedTask.setName(task.getName());
        editedTask.setCompleted(task.isCompleted());
        taskRepo.save(editedTask);
        return editedTask;
    }
}
