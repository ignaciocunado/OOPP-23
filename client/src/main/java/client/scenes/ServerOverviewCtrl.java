package client.scenes;

import client.config.Config;
import client.utils.ServerUtils;
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
    private final ServerUtils utils;
    private final Config config;
    @Inject
    public ServerOverviewCtrl(final MainCtrl ctrl, final ServerUtils utils, final Config config) {
        this.ctrl = ctrl;
        this.utils = utils;
        this.config = config;
    }

    @FXML
    public void initialize() {
        this.config.getWorkspaces().forEach(workspace -> {
            final Text server = new Text(workspace.getConnectionUri());
            server.setCursor(Cursor.HAND);
            server.setOnMouseClicked((event) -> {
                this.config.setCurrentWorkspace(workspace.getConnectionUri());
                this.utils.setServer(workspace.getConnectionUri());
                this.ctrl.showLandingOverview();
            });
            this.joinedWorkspaces.getChildren().add(server);
        });
    }

    public void connect() {
        if (!this.utils.ping(this.connectionUriField.getText())) {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("This server wasn't recognised");
            alert.show();
            return;
        }

        this.config.setCurrentWorkspace(this.connectionUriField.getText());
        this.utils.setServer(this.connectionUriField.getText());
        this.ctrl.showLandingOverview();
    }

}
