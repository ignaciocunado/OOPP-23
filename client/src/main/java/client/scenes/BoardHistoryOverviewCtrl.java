package client.scenes;

import client.Config;
import client.RecentBoard;
import client.utils.ServerUtils;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.rgb;

public class BoardHistoryOverviewCtrl implements Initializable {

    private final Config config;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private VBox keyVBox;
    @FXML
    private VBox serverVBox;
    @FXML
    private VBox actionsVBox;


    /** Constructor to inject necessary classes into the controller
     * @param mainCtrl
     * @param server
     * @param config
     */
    @Inject
    public BoardHistoryOverviewCtrl(MainCtrl mainCtrl, ServerUtils server, Config config) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
    }

    /**
     * Initialisation method initialising FXML objects
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (RecentBoard recent:config.getBoards()) {
            addAndConfigureText(recent.getKey(), keyVBox, recent.getKey());
            addAndConfigureText(recent.getServer(), serverVBox, recent.getKey());
            addAndConfigureText("Rejoin", actionsVBox, recent.getKey());
        }
    }

    /**
     * Configures the value of the Text and adds it to the corresponding VBox
     * @param stringValue the string displayed by the Text
     * @param vboxToAddTo the VBox to which the Text will be added
     * @param boardKey the key of the Board associated to the Text
     */
    public void addAndConfigureText(String stringValue, VBox vboxToAddTo, String boardKey) {
        Text newText = new Text(stringValue);
        newText.getStyleClass().add("texts");
        vboxToAddTo.getChildren().add(newText);
        if (stringValue.equals("Rejoin")) {
            newText.getStyleClass().add("rejoin");
            setOnMouseClicked(newText, boardKey);
            setOnMouseHovered(newText);
        }
    }

    /**
     * Displays the correct Board when the "Rejoin" text is pressed
     * @param rejoinText "Rejoin" text
     * @param boardKey the key of the Board which will be joined
     */
    public void setOnMouseClicked(Text rejoinText, String boardKey) {
        rejoinText.setOnMouseClicked(event -> {
            final Board board = this.server.getBoard(boardKey);
            if (board == null) {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("This board wasn't recognised");
                alert.show();
                return;
            }
            this.mainCtrl.closeHistory();
            this.mainCtrl.showBoardOverview(board);
        });
    }

    /**
     * Sets visual feedback for when mouse is hovering over the "Rejoin" text
     * @param rejoinText the "Rejoin" text
     */
    public void setOnMouseHovered(Text rejoinText) {
        rejoinText.setOnMouseEntered(event -> {
            rejoinText.setFill(rgb(52,86,120));
            rejoinText.getScene().setCursor(Cursor.HAND);
        });
        rejoinText.setOnMouseExited(event -> {
            rejoinText.setFill(WHITE);
            rejoinText.getScene().setCursor(Cursor.DEFAULT);
        });
    }
}
