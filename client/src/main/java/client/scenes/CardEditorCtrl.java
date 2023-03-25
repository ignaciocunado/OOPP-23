package client.scenes;

import client.MyFXML;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
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

    private TagCtrl tagCtrl = new TagCtrl();
    private TaskCtrl taskCtrl = new TaskCtrl();

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
        for(Tag tag : card.getTags()) {
            Pane tagPane = FXMLLoader.load(getLocation("client", "scenes", "Tag.fxml"));
            tagPane.setId(Integer.toString(tag.getId()));
            TextField name = (TextField) tagPane.getChildren().get(0);
            title.setText(tag.getName());
            tags.getChildren().add(tagPane);
        }
        for(Task task : card.getNestedTaskList()) {
            Pane taskPane = FXMLLoader.load(getLocation("client", "scenes", "Task.fxml"));
            taskPane.setId(Integer.toString(task.getId()));
            TextField title = (TextField) taskPane.getChildren().get(0);
            title.setText(task.getName());
            CheckBox checkBox = (CheckBox) taskPane.getChildren().get(1);
            if(task.isCompleted()) {
                checkBox.selectedProperty().set(true);
                checkBox.indeterminateProperty().set(false);
            }
            else {
                checkBox.selectedProperty().set(false);
                checkBox.indeterminateProperty().set(false);
            }
            taskPane.setId(Integer.toString(task.getId()));
            tags.getChildren().add(taskPane);
        }
    }

    /**
     * Saves data from new Card
     * @return card
     */
    public Card save() {
        currentCard.setTitle(this.title.getText());
        currentCard.setDescription(this.description.getText());
        mainCtrl.closeCardEditor();
        return currentCard;
    }


    /**
     * Calls method to add a task
     * @throws IOException
     */
    public void addTask() throws IOException {
        Pane taskPane = FXMLLoader.load(getLocation("client", "scenes", "Task.fxml"));
        Task task = new Task("New title", false);
        taskPane.setId(Integer.toString(task.getId()));
        nestedTaskList.getChildren().add(taskPane);
    }

    /**
     * Adds a tag to a card
     * @throws IOException
     */
    public void addTag() throws IOException {
        Pane tagPane = FXMLLoader.load(getLocation("client", "scenes", "Tag.fxml"));
        Tag tag = new Tag("New title", 0);
        tagPane.setId(Integer.toString(tag.getId()));
        tags.getChildren().add(tagPane);
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
