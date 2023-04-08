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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    @FXML
    private AnchorPane mainPane;
    private CardCtrl cardCtrl;
    private Card currentCard;
    @FXML
    private ComboBox<Tag> combo;
    @FXML
    private ColorPicker colour;
    @FXML
    private Button resetButton;
    @FXML
    private Button saveButton;

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
        setRightColours();
        mainPane.setOnKeyPressed(event1 -> {
            if (event1.getCode().equals(KeyCode.ESCAPE)) {
                this.mainCtrl.closeCardEditor();
            }
        });
        setEditCardMethods();
    }

    /**
     * Sets colour of nodes
     */
    private void setRightColours() {
        if(currentCard.getColour().equals("#123456")) {
            mainPane.setStyle("-fx-background-color:  rgb(35,69,103)");
            nestedTaskList.setStyle("-fx-background-color:  rgb(35,69,103)");
            resetButton.setStyle("-fx-padding: 0px; -fx-background-color:  #123456");
            saveButton.setStyle("-fx-padding: 0px; -fx-background-color:  #123456");
        }
        else {
            mainPane.setStyle("-fx-background-color: " + currentCard.getColour());
            nestedTaskList.setStyle("-fx-background-color: " + currentCard.getColour());
            resetButton.setStyle("-fx-padding: 0px; -fx-background-color: " + getRGBShade());
            saveButton.setStyle("-fx-padding: 0px; -fx-background-color: " + getRGBShade());
        }
        Color cardColour  = Color.web(currentCard.getColour());
        this.colour.setValue(cardColour);
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
        this.serverUtils.editCard(this.currentCard.getId(), title.getText(), description.getText(),
                this.currentCard.getColour());
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
                this.description.getText(),
                getColourString());
        mainCtrl.closeCardEditor();
        this.cardCtrl.refresh(this.currentCard);
        return currentCard;
    }

    /**
     * Converts Colour
     * @return a string rgb colour
     */
    public String getColourString() {
        Double redDouble =  colour.getValue().getRed()*255;
        Double greenDouble = colour.getValue().getGreen()*255;
        Double blueDouble =  colour.getValue().getBlue()*255;
        int red = redDouble.intValue();
        int green = greenDouble.intValue();
        int blue = blueDouble.intValue();
        return  "rgb(" + red  + "," +
                green + ", " +
                blue + ")";
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

    /**
     * Resets the colour of the card to its default settings
     */
    public void resetColour() {
        serverUtils.editCard(currentCard.getId(), currentCard.getTitle(),
                currentCard.getDescription(), "#123456");
        mainCtrl.closeCardEditor();
        this.cardCtrl.refresh(this.currentCard);
    }

    /**
     * Gets a shade of RGB to add to panes and buttons
     * @return string representing RGB colour
     */
    public String getRGBShade() {
        Color color = Color.web(currentCard.getColour());
        double newRed = color.getRed()*0.75*255;
        double newGreen = color.getGreen()*0.75*255;
        double newBlue = color.getBlue()*0.75*255;
        return "rgb(" + newRed + "," + newGreen + "," + newBlue + ")";
    }
}
