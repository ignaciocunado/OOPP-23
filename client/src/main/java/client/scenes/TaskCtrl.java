package client.scenes;

import client.MyFXML;
import commons.entities.Card;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    private int counter = 0;

    /**
     * Constructor
     */
    public TaskCtrl() {

    }

    /**
     * Adds a task to a card
     * @param currentCard the card to add the task to
     * @param box box in which to add the task
     * @throws IOException
     */
    public void addnewTask(Card currentCard, VBox box) throws IOException {
        Pane taskPane = FXMLLoader.load(getLocation("client", "scenes", "Task.fxml"));
        box.getChildren().add(taskPane);
    }

    /**
     * Removes a task from a card
     */
    public void removeTask() {
        
    }

    /**
     * Renders existing tasks onto the card view
     */
    public void renderExistingTaks() {

    }

    /**
     * Edits a task
     */
    public void editTask() {

    }

    /**
     * Gets the location of a resource with the given String elements
     * @param parts Strings of where to find the resource
     * @return the URL of the requested resource
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }
}
