package client.scenes;

import commons.entities.Card;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class TaskCtrl {

    @FXML
    private CheckBox completed;

    @FXML
    private TextField title;
    private Card currentCard;

    /**
     * Constructor
     */
    public TaskCtrl() {

    }

    /**
     * Removes a task from a card
     */
    public void removeTask() {
        
    }
}
