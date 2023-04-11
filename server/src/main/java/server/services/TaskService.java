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

    /** constructor for taskService
     * @param taskRepo repository for task
     * @param cardRepo repository for card
     */
    public TaskService(TaskRepository taskRepo, CardRepository cardRepo) {
        this.taskRepo = taskRepo;
        this.cardRepo = cardRepo;
    }

    /** method to edit a task
     * @param id it of the task
     * @param task the task that will be edited
     * @return the edited task
     */
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
    /**
     * endpoint for changing the order of tasks
     *
     * @param id        integer representing the id of the task
     * @param direction the direction to move the tag in
     * @return returns the card needed for the websockets
     */
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
