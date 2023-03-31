package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

public class AdminPasswordCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private PasswordField passwordField;
    String adminPassword = "admin";

    /**
     * The wrapping controller for a card list
     *
     * @param server the server functions
     * @param mainCtrl the main controller
     */
    @Inject
    public AdminPasswordCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {

    }

    public void checkPassword() {
        if (adminPassword.equals(passwordField.getText())) {
            passwordField.setText("");
            this.mainCtrl.closeAdminPassword();
            this.mainCtrl.showHistoryAdmin();
        }
        else {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect Password");
            alert.show();
        }
    }
}
