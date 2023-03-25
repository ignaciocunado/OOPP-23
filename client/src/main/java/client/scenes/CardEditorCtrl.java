package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class CardEditorCtrl implements Initializable{

    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private HBox tags;
    @FXML
    private VBox nestedTaskList;
    private Card currentCard;
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;


    /**
     * Constructor
     * @param mainCtrl mainCtrl
     * @param serverUtils serverUtils
     */
    @Inject
    public CardEditorCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Refreshes card editor info
     * @param card current Card
     */
    public void refresh(Card card) throws IOException {
        this.currentCard = card;
        this.title.setText(this.currentCard.getTitle());
        this.description.setText(this.currentCard.getDescription());
        tags.getChildren().removeAll(tags.getChildren());
        nestedTaskList.getChildren().removeAll(nestedTaskList.getChildren());
        for(Tag tag : currentCard.getTags()) {
            FXMLLoader loader = new FXMLLoader();
            Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
            TagCtrl ctrl = loader.getController();
            tagPane.setId(Integer.toString(tag.getId()));
            ctrl.editData(tag);
            tags.getChildren().add(tagPane);
        }
        for(Task task: currentCard.getNestedTaskList()) {
            FXMLLoader loader = new FXMLLoader();
            Pane taskPane = loader.load(getClass().getResource("Task.fxml").openStream());
            taskPane.setId(Integer.toString(task.getId()));
            TaskCtrl ctrl = loader.getController();
            ctrl.editData(task);
            taskPane.setId(Integer.toString(task.getId()));
            nestedTaskList.getChildren().add(taskPane);
        }
    }

    /**
     * Saves data from new Card
     * @return card
     */
    public Card save() {
        currentCard.setTitle(this.title.getText());
        currentCard.setDescription(this.description.getText());
        /*
        tagCtrl.addOrUpdateTags(currentCard, tags);
        taskCtrl.addOrUpdateTasks(currentCard, nestedTaskList);
        */
        currentCard.addTag(new Tag("New taddsdsdsdg", 3125329));
        mainCtrl.closeCardEditor();
        return currentCard;
    }


    /**
     * Calls method to add a task
     * @throws IOException
     */
    public void addTask() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane taskPane = loader.load(getClass().getResource("Task.fxml").openStream());
        TaskCtrl ctrl = loader.getController();
        Task task = new Task("New title", false);
        taskPane.setId(Integer.toString(task.getId()));
        nestedTaskList.getChildren().add(taskPane);
    }

    /**
     * Adds a tag to a card
     * @throws IOException
     */
    public void addTag() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
        TagCtrl ctrl = loader.getController();
        Tag tag = new Tag("New title", 0);
        tagPane.setId(Integer.toString(tag.getId()));
        tags.getChildren().add(tagPane);
    }

}
