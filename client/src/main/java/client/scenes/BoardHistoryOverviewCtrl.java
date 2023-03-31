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

    public void refresh() {
        this.servers.getChildren().clear();
        for (RecentBoard recent:config.getCurrentWorkspace().getBoards()) {
            final HBox serverBox = new HBox();
            final Text key = new Text(recent.getKey());
            final Text server = new Text(config.getCurrentWorkspace().getConnectionUri());
            final Text rejoin = new Text("rejoin");
            key.getStyleClass().add("texts");
            server.getStyleClass().add("texts");
            rejoin.getStyleClass().add("texts");
            key.setWrappingWidth(150);
            server.setWrappingWidth(150);
            rejoin.setWrappingWidth(150);

            rejoin.getStyleClass().add("rejoin");
            setOnMouseClicked(rejoin, recent.getKey());
            setOnMouseHovered(rejoin);


            serverBox.setSpacing(15);
            serverBox.getChildren().addAll(key, server, rejoin);
            this.servers.getChildren().add(serverBox);
        }
    }

    /**
     * Displays the correct Board when the "Rejoin" text is pressed
     * @param rejoinText "Rejoin" text
     * @param boardKey the key of the Board which will be joined
     */
    public void setOnMouseClicked(Text rejoinText, String boardKey) {
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
