package client.scenes;

import client.config.Config;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public final class ServerOverviewCtrl {

    @FXML
    private TextField connectionUriField;

    private final MainCtrl ctrl;
    private final ServerUtils utils;
    private final Config config;
    @Inject
    public ServerOverviewCtrl(final MainCtrl ctrl, final ServerUtils utils, final Config config) {
        this.ctrl = ctrl;
        this.utils = utils;
        this.config = config;
    }

    public void connect() {
        this.config.setCurrentWorkspace(this.connectionUriField.getText());
        this.utils.setServer(this.connectionUriField.getText());
        this.ctrl.showLandingOverview();
    }

}
