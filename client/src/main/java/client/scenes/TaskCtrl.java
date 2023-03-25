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
/*
    @FXML
    private CheckBox completed;

    @FXML
    private TextField title;
    private Card currentCard;
*/
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

    public void renderTask(Card currentCard, VBox nestedTaskList) throws IOException {
        for(Task task: currentCard.getNestedTaskList()) {
            Pane taskPane = FXMLLoader.load(getLocation("client", "scenes", "Task.fxml"));
            taskPane.setId(Integer.toString(task.getId()));
            TextField title = (TextField) taskPane.getChildren().get(0);
            title.setText(task.getName());
            CheckBox completed = (CheckBox) taskPane.getChildren().get(1);
            if(task.isCompleted()) {
                completed.selectedProperty().set(true);
                completed.indeterminateProperty().set(false);
            }
            else {
                completed.selectedProperty().set(false);
                completed.indeterminateProperty().set(false);
            }
            taskPane.setId(Integer.toString(task.getId()));
            nestedTaskList.getChildren().add(taskPane);
        }
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
