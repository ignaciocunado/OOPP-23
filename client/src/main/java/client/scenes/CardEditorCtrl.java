package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;


/**
 * Please note that not all methods work as at the moment all the tagIDS are 0.
 * When connected to server, these will work.
 */
public class CardEditorCtrl {
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private HBox tags;
    @FXML
    private VBox nestedTaskList;

    private CardCtrl cardCtrl;
    private Card currentCard;
    @FXML
    ComboBox combo;


    /**
     * Constructor
     *
     * @param serverUtils serverUtils
     * @param mainCtrl    mainCtrl
     */
    @Inject
    public CardEditorCtrl(final ServerUtils serverUtils, final MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Refreshes card editor info
     * @param cardCtrl current Card
     */
    public void refresh(final CardCtrl cardCtrl) {
        this.currentCard = cardCtrl.getCard();
        this.cardCtrl = cardCtrl;
        combo.getItems().clear();
        combo.getItems().addAll(this.cardCtrl.getBoard().getTags());
        this.title.setText(this.currentCard.getTitle());
        this.description.setText(this.currentCard.getDescription());
        try {
            tags.getChildren().clear();
            for (Tag tag : currentCard.getTags()) {
                FXMLLoader loader = new FXMLLoader();
                Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
                TagCtrl ctrl = loader.getController();
                tagPane.setId(Integer.toString(tag.getId()));
                ctrl.editData(tag);
                ctrl.update(tag.getId(), this);
                tags.getChildren().add(tagPane);
            }

            nestedTaskList.getChildren().clear();
            for (Task task : currentCard.getNestedTaskList()) {
                FXMLLoader loader = new FXMLLoader();
                Pane taskPane = loader.load(getClass().getResource("Task.fxml").openStream());
                taskPane.setId(Integer.toString(task.getId()));
                TaskCtrl ctrl = loader.getController();
                ctrl.editData(task);
                taskPane.setId(Integer.toString(task.getId()));
                ctrl.update(task.getId(), this, task);
                nestedTaskList.getChildren().add(taskPane);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setEditCardMethods();
    }

    /**
     * Instantiates edit methods on the title and description
     */
    private void setEditCardMethods() {
        title.focusedProperty().addListener(this::titleOrDescriptionEdited);
        description.focusedProperty().addListener(this::titleOrDescriptionEdited);
    }

    /**
     * Checks whether any of the fields lost focus and calls the editCard endpoint
     * @param observable lame ass parameter
     */
    private void titleOrDescriptionEdited(Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if (focused.getValue()) return; // If focuses then don't save yet
        this.serverUtils.editCard(this.currentCard.getId(), title.getText(), description.getText());
    }

    /**
     * Saves data from new Card
     * @return card
     */
    public Card save() {
        currentCard.setTitle(this.title.getText());
        currentCard.setDescription(this.description.getText());
        this.serverUtils.editCard(
                this.currentCard.getId(),
                this.title.getText(),
                this.description.getText()
        );
        mainCtrl.closeCardEditor();
        this.cardCtrl.refresh(this.currentCard);
        return currentCard;
    }


    /**
     * Adds a task to a card
     * @throws IOException
     */
    public void addTask() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane taskPane = loader.load(getClass().getResource("Task.fxml").openStream());
        TaskCtrl ctrl = loader.getController();
        currentCard = serverUtils.addTaskToCard(this.currentCard.getId(),"Title",
            false);
        Task task = currentCard.getNestedTaskList().get(currentCard.getNestedTaskList().size() - 1);
        taskPane.setId(Integer.toString(task.getId()));
        ctrl.update(task.getId(), this, task);
        nestedTaskList.getChildren().add(taskPane);
    }

    /**
     * Adds a tag to a card
     * @throws IOException
     */
    public void addTag() throws IOException {
        Tag tag = (Tag) combo.getValue();
        if(currentCard.getTags().contains(tag)) {
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        Pane tagPane = loader.load(getClass().getResource("Tag.fxml").openStream());
        TagCtrl ctrl = loader.getController();
        tagPane.setId(Integer.toString(tag.getId()));
        ctrl.update(tag.getId(), this);
        ctrl.editData(tag);
        tags.getChildren().add(tagPane);
        this.currentCard = serverUtils.addTag(tag.getId(), this.currentCard.getId(),tag);
    }

    /**
     * Removes rendered Tag with the specified id if it exists
     * @param id id of the tag to remove
     */
    public void removeTag(int id) {
        tags.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
        this.currentCard = serverUtils.removeTagFromCard(currentCard.getId(), id);
    }

    /**
     * Removes rendered Task with the specified id if it exists
     * @param id id of the task to remove
     */
    public void removeTask(int id) {
        nestedTaskList.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
        this.currentCard = serverUtils.removeTaskFromCard(id, currentCard.getId());
    }

    /**
     * Handles Tasks edits
     * @param taskId id of the task to edit
     * @param title new title of the task
     * @param isTaskCompleted boolean representing completeness of the task
     */
    public void editTask(int taskId, String title, boolean isTaskCompleted) {
        this.serverUtils.editTask(taskId, title,
            isTaskCompleted);
    }
}
