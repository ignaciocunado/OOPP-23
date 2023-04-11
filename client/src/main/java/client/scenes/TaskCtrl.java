package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Task;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class TaskCtrl {

    private ServerUtils server;

    @FXML
    private TextField title;
    @FXML
    private CheckBox completed;
    private int id;
    private CardEditorCtrl cardEditorCtrl;
    private Task task;

    /**
     * Constructor to inject server utils into the task
     * @param server the server utils
     */
    @Inject
    public TaskCtrl(final ServerUtils server) {
        this.server = server;
    }


    /**
     * Removes a task from a card
     */
    public void removeTask() {
        cardEditorCtrl.removeTask(id);
    }

    /**
     * Edits the data of the rendered Task
     * @param task task that this rendered object represents
     */
    public void editData(Task task) {
        this.title.setText(task.getName());
        if(task.isCompleted()) {
            completed.selectedProperty().set(true);
            completed.indeterminateProperty().set(false);
        }
        else {
            completed.selectedProperty().set(false);
            completed.indeterminateProperty().set(false);
        }
    }

    /**
     * Update methods
     * @param id id of the rendered object
     * @param cardEditorCtrl ctrl
     * @param task task that this represents
     */
    public void update(int id, CardEditorCtrl cardEditorCtrl, Task task){
        this.id = id;
        this.cardEditorCtrl = cardEditorCtrl;
        this.title.focusedProperty().addListener(this::titleEdited);
        this.title.setText(task.getName());
        this.task = task;
        this.completed.setOnAction(event -> {
            boolean isTaskCompleted = !completed.isIndeterminate() && completed.isSelected();
            cardEditorCtrl.editTask(this.task.getId(), title.getText(), isTaskCompleted);
            task.setCompleted(isTaskCompleted);
        });
    }

    private void titleEdited(Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if (focused.getValue()) return; // If focuses then don't save yet
        boolean isTaskCompleted = !completed.isIndeterminate() && completed.isSelected();
        task.setName(title.getText());
        cardEditorCtrl.editTask(this.task.getId(), title.getText(), isTaskCompleted);
    }

    @FXML
    private void moveUp() {
        this.server.moveTask(this.id, "up");
    }

    @FXML
    private void moveDown() {
        this.server.moveTask(this.id, "down");
    }

}
