package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Please note that not all methods work as at the moment all the tagIDS are 0.
 * When connected to server, these will work.
 */
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
    @FXML
    ComboBox combo;


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
        combo.getItems().addAll(currentCard.getTags());
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
            ctrl.update(tag.getId(), this);
            tags.getChildren().add(tagPane);
        }
        for(Task task: currentCard.getNestedTaskList()) {
            FXMLLoader loader = new FXMLLoader();
            Pane taskPane = loader.load(getClass().getResource("Task.fxml").openStream());
            taskPane.setId(Integer.toString(task.getId()));
            TaskCtrl ctrl = loader.getController();
            ctrl.editData(task);
            taskPane.setId(Integer.toString(task.getId()));
            ctrl.update(task.getId(), this);
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

        for(Node node : nestedTaskList.getChildren()) {
            Pane pane = (Pane) node;
            TextField textField = (TextField) pane.getChildren().get(0);
            String name = textField.getText();
            CheckBox checkBox = (CheckBox) pane.getChildren().get(1);
            boolean completed;
            if(checkBox.isSelected() && !checkBox.isIndeterminate()) {
                completed = true;
            }
            else {
                completed = false;
            }
            int id = Integer.parseInt(pane.getId());
            List<Task> found = currentCard.getNestedTaskList().stream().filter(task1 ->
                task1.getId() == id).toList();
            if(found.size() == 0) {
                Task newTask = new Task(name, completed);
                newTask.setId(id);
                currentCard.addTask(newTask);
            }
            else {
                Task task = found.get(0);
                task.setName(name);
                task.setCompleted(completed);
            }
        }

        for(Node node : tags.getChildren()) {
            Pane pane = (Pane) node;
            TextField textField = (TextField) pane.getChildren().get(0);
            String name = textField.getText();
            int id = Integer.parseInt(pane.getId());
            List<Tag> found = currentCard.getTags().stream().filter(tag1 ->
                tag1.getId() == id).toList();
            if(found.size() == 0) {
                //add tag with id id;
            }
        }
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
        ctrl.update(task.getId(), this);
        nestedTaskList.getChildren().add(taskPane);
    }

    /**
     * Adds a tag to a card
     * @throws IOException
     */
    public void addTag() throws IOException {
        Tag tag = (Tag) combo.getValue();
        FXMLLoader loader = new FXMLLoader();
        Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
        TagCtrl ctrl = loader.getController();
        tagPane.setId(Integer.toString(tag.getId()));
        ctrl.update(tag.getId(), this);
        tags.getChildren().add(tagPane);
    }

    /**
     * Removes rendered Tag with the specified id if it exists
     * @param id id of the tag to remove
     */
    public void removeTag(int id){
        tags.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
    }

    /**
     * Removes rendered Task with the specified id if it exists
     * @param id id of the task to remove
     */
    public void removeTask(int id){
        nestedTaskList.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
    }

}
