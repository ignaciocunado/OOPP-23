package client.scenes;

import client.config.Config;
import client.config.RecentBoard;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.rgb;

public class BoardHistoryOverviewCtrl {

    private final Config config;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private VBox servers;


    /** Constructor to inject necessary classes into the controller
     * @param mainCtrl the main controller
     * @param server the server functions
     * @param config the config file
     */
    @Inject
    public BoardHistoryOverviewCtrl(MainCtrl mainCtrl, ServerUtils server, Config config) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
    }

    /**
     * Initialisation method initialising FXML objects
     */
    @FXML
    public void initialize() {

    }

    /**
     * Refreshes the board history view with old recent boards read from the config
     */
    public void refresh() {
        this.servers.getChildren().clear();
        for (RecentBoard recent:config.getCurrentWorkspace().getBoards()) {
            addTexts(getBoard(recent.getKey()));
        }
    }

    /**
     * Gets the board given a key
     * @param key the key used to look for the board
     * @return the Board corresponding to the key
     */
    public Board getBoard(String key) {
        return this.server.getBoard(key);
    }

    /**
     * Adds all the Texts and the appropriate actions for each Board
     * @param board the Board being added to the history
     */
    public void addTexts(Board board) {
        final HBox serverBox = new HBox();
        final Text key = new Text(board.getKey());
        final Text boardName = new Text(board.getName());
        final Text rejoin = new Text("Rejoin");
        final Text delete = new Text("Delete");
        final Text empty = new Text("");

        setOnMouseClickedRejoin(rejoin, board.getKey());
        setOnMouseClickedDelete(delete, board.getKey());
        setOnMouseHovered(rejoin);
        setOnMouseHovered(delete);

        serverBox.setSpacing(15);
        serverBox.getChildren().addAll(key, boardName, empty, rejoin, delete);
        for (Node child: serverBox.getChildren()) {
            addStyleClass((Text) child);
        }
        this.servers.getChildren().add(serverBox);
    }

    /**
     * Adds the styling to the Text
     * @param txt the text to style
     */
    public void addStyleClass(Text txt) {
        txt.getStyleClass().add("texts");
        if (txt.getText().equals("Rejoin")|| txt.getText().equals("Delete")) {
            txt.setWrappingWidth(50);
            txt.getStyleClass().add("interactiveHistoryTexts");
        }
        else if (txt.getText().equals("")) {
            txt.setWrappingWidth(7);
        }
        else {
            txt.setWrappingWidth(150);
        }
    }

    /**
     * Deletes the correct Board when the "Delete" text is pressed
     * @param delete "Delete" text
     * @param boardKey the key of the Board which will be joined
     */
    private void setOnMouseClickedDelete(Text delete, String boardKey) {
        delete.setOnMouseClicked(event -> {
            this.server.deleteBoard(boardKey);
            config.getCurrentWorkspace().deleteBoard(boardKey);
            refresh();
        });
    }

    /**
     * Displays the correct Board when the "Rejoin" text is pressed
     * @param rejoinText "Rejoin" text
     * @param boardKey the key of the Board which will be joined
     */
    public void setOnMouseClickedRejoin(Text rejoinText, String boardKey) {
        rejoinText.setOnMouseClicked(event -> {
            this.config.getCurrentWorkspace().addBoard(boardKey);
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
     * Sets visual feedback for when mouse is hovering over the "Rejoin" and "Delete" text
     * @param txt the "Rejoin" or "Delete" text
     */
    public void setOnMouseHovered(Text txt) {
        txt.setOnMouseEntered(event -> {
            txt.setFill(rgb(52,86,120));
            txt.getScene().setCursor(Cursor.HAND);
        });
        txt.setOnMouseExited(event -> {
            txt.setFill(WHITE);
            txt.getScene().setCursor(Cursor.DEFAULT);
        });
    }
}
