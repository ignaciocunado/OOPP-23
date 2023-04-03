package client.scenes;

import client.UserState;
import client.config.Config;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.rgb;

public class AdminOverviewCtrl {

    private final Config config;
    private final UserState state;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private String password;
    @FXML
    private VBox boards;


    /** Constructor to inject necessary classes into the controller
     * @param mainCtrl the main controller
     * @param server the server functions
     * @param config the config file
     * @param state the state of the user (admin or not)
     */
    @Inject
    public AdminOverviewCtrl(MainCtrl mainCtrl, ServerUtils server,
                             Config config, UserState state) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.state = state;
    }

    /**
     * Refreshes the board history view with old recent boards read from the config
     */
    public void refresh() throws IOException {
        this.boards.getChildren().clear();


        if (this.server.getAllBoards(this.state.getPassword()) != null) {
            for (Board recent:this.server.getAllBoards(this.state.getPassword())) {
                addTexts(recent.getKey());
            }
        }
    }

    /**
     * Adds all the Texts and the appropriate actions for each Board
     * @param recentKey the key of the Board being added to the history
     */
    public void addTexts(String recentKey) {
        final HBox serverBox = new HBox();
        final Text key = new Text(recentKey);
        final Text server = new Text(config.getCurrentWorkspace().getConnectionUri());
        final Text rejoin = new Text("Rejoin");
        final Text delete = new Text("Delete");
        final Text empty = new Text("");
        key.getStyleClass().add("texts");
        server.getStyleClass().add("texts");
        rejoin.getStyleClass().add("texts");
        delete.getStyleClass().add("texts");
        key.setWrappingWidth(150);
        server.setWrappingWidth(150);
        empty.setWrappingWidth(7);
        rejoin.setWrappingWidth(50);
        delete.setWrappingWidth(50);

        rejoin.getStyleClass().add("interactiveHistoryTexts");
        delete.getStyleClass().add("interactiveHistoryTexts");
        setOnMouseClickedRejoin(rejoin, recentKey);
        setOnMouseClickedDelete(delete, recentKey);
        setOnMouseHovered(rejoin);
        setOnMouseHovered(delete);

        serverBox.setSpacing(15);
        serverBox.getChildren().addAll(key, server, empty, rejoin, delete);
        this.boards.getChildren().add(serverBox);
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
            try {
                refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
