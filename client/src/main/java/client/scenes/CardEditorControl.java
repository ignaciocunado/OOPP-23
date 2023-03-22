package client.scenes;

import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardEditorControl implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private HBox tags;
    @FXML
    private VBox nestedTaskList;
    private Card currentCard;

    public CardEditorControl(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addTag() {

    }

    public void addTask() {

    }

    public void saveCard() {
        String newTitle;
        String newDescription;
        ArrayList<Tag> newTagList;
        ArrayList<Task> newNestedTaskList;
        Card newCard = new Card(newTitle,newDescription,newTagList,newNestedTaskList);
    }
}
