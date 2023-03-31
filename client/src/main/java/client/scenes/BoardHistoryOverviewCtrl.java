package client.scenes;

import client.config.Config;
import client.config.RecentBoard;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
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
    @FXML
    public void initialize() {

    }

    /**
     * Refreshes the board history view with old recent boards read from the config
     */
    public void refresh() {
        this.servers.getChildren().clear();
        for (RecentBoard recent:config.getCurrentWorkspace().getBoards()) {
            final HBox serverBox = new HBox();
            final Text key = new Text(recent.getKey());
            final Text server = new Text(config.getCurrentWorkspace().getConnectionUri());
            final Text rejoin = new Text("Rejoin");
            key.getStyleClass().add("texts");
            server.getStyleClass().add("texts");
            rejoin.getStyleClass().add("texts");
            key.setWrappingWidth(150);
            server.setWrappingWidth(150);
            rejoin.setWrappingWidth(150);

            rejoin.getStyleClass().add("rejoin");
            setOnMouseClickedRejoin(rejoin, recent.getKey());
            setOnMouseHovered(rejoin);

            serverBox.setSpacing(15);
            serverBox.getChildren().addAll(key, server, rejoin);
            this.servers.getChildren().add(serverBox);
        }
    }

    public void refreshAdmin() {
        this.servers.getChildren().clear();
        for (Board recent:this.server.getAllBoards()) {
            final HBox serverBox = new HBox();
            final Text key = new Text(recent.getKey());
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

            rejoin.getStyleClass().add("rejoin");
            delete.getStyleClass().add("rejoin");
            setOnMouseClickedRejoin(rejoin, recent.getKey());
            setOnMouseClickedDelete(delete, recent.getKey());
            setOnMouseHovered(rejoin);
            setOnMouseHovered(delete);

            serverBox.setSpacing(15);
            serverBox.getChildren().addAll(key, server, empty, rejoin, delete);
            this.servers.getChildren().add(serverBox);
        }
    }

    private void setOnMouseClickedDelete(Text delete, String key) {
        delete.setOnMouseClicked(event -> {
            this.server.deleteBoard(key);
            config.getCurrentWorkspace().deleteBoard(key);
            refreshAdmin();
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
     * Sets visual feedback for when mouse is hovering over the "Rejoin" text
     * @param txt the "Rejoin" text
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
