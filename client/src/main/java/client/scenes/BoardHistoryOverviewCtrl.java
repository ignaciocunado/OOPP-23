package client.scenes;

import client.Config;
import client.RecentBoard;
import client.utils.ServerUtils;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardHistoryOverviewCtrl implements Initializable {

    private Config config;
    private MainCtrl mainCtrl;
    private ServerUtils server;
    @FXML
    private VBox keyvbox;
    @FXML
    private VBox servervbox;
    @FXML
    private VBox actionsvbox;
    @FXML
    private AnchorPane anchorPane;


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
            System.out.println(recent.getKey());
            keyvbox.getChildren().add(new Text(recent.getKey()));
            servervbox.getChildren().add(new Text(recent.getServer()));
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
                this.mainCtrl.showBoardOverview(board);
            });
            actionsvbox.getChildren().add(joinText);
        }
    }

    /**
     * refreshes the BoardHistoryOverviewCtrl
     */
    public void refresh() {
    }
}
