package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    public void refresh(Card card) {
        this.currentCard = card;
        this.title.setText(this.currentCard.getTitle());
        this.description.setText(this.currentCard.getDescription());
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
     * Calls method to add a tag
     * @throws IOException
     */
    public void addTag() throws IOException {
        tagCtrl.addTag(currentCard, tags);
    }

    /**
     * Calls method to add a task
     * @throws IOException
     */
    public void addTask() throws IOException {
        taskCtrl.addTask(currentCard, nestedTaskList);
    }
}
