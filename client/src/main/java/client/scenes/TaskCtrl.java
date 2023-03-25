package client.scenes;

import client.MyFXML;
import commons.entities.Card;
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
