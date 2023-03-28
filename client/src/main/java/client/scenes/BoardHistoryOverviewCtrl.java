package client.scenes;

import client.Config;
import client.RecentBoard;
import client.utils.ServerUtils;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardHistoryOverviewCtrl implements Initializable {

    private Config config;
    private MainCtrl mainCtrl;
    private ServerUtils server;
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
            Text keyText = new Text(recent.getKey());
            keyText.setWrappingWidth(70);
            keyText.setTextAlignment(TextAlignment.CENTER);
            keyText.setFill(Color.WHITE);
            keyVBox.getChildren().add(keyText);
            Text serverText = new Text(recent.getServer());
            serverText.setWrappingWidth(125);
            serverText.setTextAlignment(TextAlignment.CENTER);
            serverText.setFill(Color.WHITE);
            serverVBox.getChildren().add(serverText);
            Text joinText = new Text("Rejoin");
            joinText.setOnMouseClicked(event -> {
                final Board board = this.server.getBoard(recent.getKey());
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
            joinText.setWrappingWidth(145);
            joinText.setTextAlignment(TextAlignment.CENTER);
            joinText.setFill(Color.WHITE);
            actionsVBox.getChildren().add(joinText);
        }
    }
}
