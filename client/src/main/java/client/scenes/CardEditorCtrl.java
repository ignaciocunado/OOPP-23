package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Optional;


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
     * @param mainCtrl mainCtrl
     * @param serverUtils serverUtils
     */
    @Inject
    public CardEditorCtrl(final ServerUtils serverUtils, final MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Refreshes card editor info
     *
     * @param cardCtrl current Card
     */
    public void refresh(final CardCtrl cardCtrl) {
        this.currentCard = cardCtrl.getCard();
        this.cardCtrl = cardCtrl;

//        combo.getItems().addAll(currentCard.getTags()); TODO: These should come from the board
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
                ctrl.update(task.getId(), this);
                nestedTaskList.getChildren().add(taskPane);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves data from new Card
     *
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

        for (Node node : nestedTaskList.getChildren()) {
            final Pane pane = (Pane) node;
            final TextField textField = (TextField) pane.getChildren().get(0);
            final String name = textField.getText();
            final CheckBox checkBox = (CheckBox) pane.getChildren().get(1);
            final boolean completed = checkBox.isSelected() && !checkBox.isIndeterminate();
            final int id = Integer.parseInt(pane.getId());
            final Optional<Task> taskOpt =
                    this.currentCard
                            .getNestedTaskList()
                            .stream()
                            .filter(task -> task.getId() == id)
                            .findAny();
            if (taskOpt.isEmpty()) {
                // TODO: This should send a request to create a new task
                Task newTask = new Task(name, completed);
                newTask.setId(id);
                this.currentCard.addTask(newTask);
                break;
            }
            // TODO: This should send a request to edit a task
            final Task task = taskOpt.get();
            task.setName(name);
            task.setCompleted(completed);
        }

        for (Node node : tags.getChildren()) {
            Pane pane = (Pane) node;
            int id = Integer.parseInt(pane.getId());
            // TODO: This should send a request to assign this tag to the board
        }
        mainCtrl.closeCardEditor();
        this.cardCtrl.refresh(this.currentCard);
        return currentCard;
    }


    /**
     * Calls method to add a task
     *
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
     *
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
     *
     * @param id id of the tag to remove
     */
    public void removeTag(int id) {
        tags.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
    }

    /**
     * Removes rendered Task with the specified id if it exists
     *
     * @param id id of the task to remove
     */
    public void removeTask(int id) {
        nestedTaskList.getChildren().removeIf(pane -> Integer.parseInt(pane.getId()) == id);
    }

}
