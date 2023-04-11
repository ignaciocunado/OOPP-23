package server.services;

import commons.entities.Card;
import commons.entities.Task;
import org.springframework.stereotype.Service;
import server.database.CardRepository;
import server.database.TaskRepository;
import server.exceptions.EntityNotFoundException;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepo;
    private final CardRepository cardRepo;

    public TaskService(TaskRepository taskRepo, CardRepository cardRepo) {
        this.taskRepo = taskRepo;
        this.cardRepo = cardRepo;
    }
    public Task editTask(int id, Task  task){
        if (!taskRepo.existsById(id)) {
            throw new EntityNotFoundException("No tag with id " + id);
        }

        final Task editedTask = taskRepo.getById(id);
        editedTask.setName(task.getName());
        editedTask.setCompleted(task.isCompleted());
        taskRepo.save(editedTask);
        return editedTask;
    }
    public Card move(Integer id, String direction){
        final Optional<Card> cardOpt = this.cardRepo.findByTaskId(id);
        if (cardOpt.isEmpty()) {
            throw new EntityNotFoundException("No card with task id " + id);
        }
        final Card card = cardOpt.get();

        // If card exists with tag then tag exists obviously
        final Task task = this.taskRepo.getById(id);

        final int currentPosition = card.getNestedTaskList().indexOf(task);

        switch (direction) {
            case "up":
                if (currentPosition == 0) return card;

                final Task previousTask = card.getNestedTaskList().get(currentPosition - 1);
                card.getNestedTaskList().set(currentPosition - 1, task);
                card.getNestedTaskList().set(currentPosition, previousTask);
                break;
            case "down":
                if (currentPosition == card.getNestedTaskList().size() - 1) return card;

                final Task nextTask = card.getNestedTaskList().get(currentPosition + 1);
                card.getNestedTaskList().set(currentPosition + 1, task);
                card.getNestedTaskList().set(currentPosition, nextTask);
                break;
        }
        this.cardRepo.save(card);
        return card;
    }
}
