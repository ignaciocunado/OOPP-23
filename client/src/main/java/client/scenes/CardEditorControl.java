package client.scenes;

import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardEditorControl {

    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private HBox tags;
    @FXML
    private VBox nestedTaskList;
    private Card currentCard;

    public CardEditorControl() {

    }

    public Tag addTag() {

    }

    public Task addTask() {

    }

    public Card saveCard() {
        String newTitle;
        String newDescription;
        ArrayList<Tag> newTagList;
        ArrayList<Task> newNestedTaskList;
        Card newCard = new Card(newTitle,newDescription,newTagList,newNestedTaskList);
    }

}
