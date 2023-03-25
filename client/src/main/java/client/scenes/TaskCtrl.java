package client.scenes;

import client.MyFXML;
import commons.entities.Card;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class TaskCtrl {

    @FXML
    private TextField title;
    @FXML
    private CheckBox completed;


    /**
     * Removes a task from a card
     */
    public void removeTask() {
        
    }

    public void addOrUpdateTasks(Card currentCard, VBox nestedTaskList) {
    }

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
}
