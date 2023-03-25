package client.scenes;

import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class TaskCtrl {

    @FXML
    private TextField title;
    @FXML
    private CheckBox completed;
    private int id;
    private CardEditorCtrl cardEditorCtrl;

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
     */
    public void update(int id, CardEditorCtrl cardEditorCtrl){
        this.id = id;
        this.cardEditorCtrl = cardEditorCtrl;
    }
}
