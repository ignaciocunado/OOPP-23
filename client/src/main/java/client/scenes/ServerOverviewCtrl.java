package client.scenes;

import client.config.Config;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public final class ServerOverviewCtrl {

    @FXML
    private TextField connectionUriField;
    @FXML
    private VBox joinedWorkspaces;

    private final MainCtrl ctrl;
    private final ServerUtils serverUtils;
    private final WebsocketUtils websocketUtils;
    private final Config config;

    /**
     * Creates a new instance of the server overview controller
     *
     * @param ctrl           the main controller for scene information
     * @param serverUtils    the server utilities to use
     * @param websocketUtils the websocket utils
     * @param config         the config file with local data
     */
    @Inject
    public ServerOverviewCtrl (final MainCtrl ctrl,
                               final ServerUtils serverUtils,
                               final WebsocketUtils websocketUtils,
                               final Config config) {
        this.ctrl = ctrl;
        this.serverUtils = serverUtils;
        this.websocketUtils = websocketUtils;
        this.config = config;
    }

    /**
     * Initializes the server menu with all saved servers
     */
    @FXML
    public void initialize() {
        this.config.getWorkspaces().forEach(workspace -> {
            final Text server = new Text(workspace.getConnectionUri());
            server.setCursor(Cursor.HAND);
            server.setOnMouseClicked((event) -> {
                this.config.setCurrentWorkspace(workspace.getConnectionUri());
                this.serverUtils.setServer("http://" + workspace.getConnectionUri());
                this.websocketUtils.disconnect();
                this.websocketUtils.connect("ws://" + workspace.getConnectionUri() + "websocket");
                this.ctrl.showLandingOverview();
            });
            this.joinedWorkspaces.getChildren().add(server);
        });
    }

    /**
     * Sets the current workspace to the one matching the uri
     * and sets the server uri in the server utils
     * then sends the user to the landing overview
     */
    public void connect () {
        if (!this.serverUtils.ping("http://" + this.connectionUriField.getText())) {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("This server wasn't recognised");
            alert.show();
            return;
        }

        this.config.setCurrentWorkspace(this.connectionUriField.getText());
        this.serverUtils.setServer("http://" + this.connectionUriField.getText());
        this.websocketUtils.disconnect();
        this.websocketUtils.connect("ws://" + this.connectionUriField.getText() + "websocket");
        this.ctrl.showLandingOverview();
    }

}
