package client.scenes;

import client.UserState;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

public class AdminPasswordCtrl {

    private final MainCtrl mainCtrl;
    private final UserState state;
    private final ServerUtils server;
    @FXML
    private PasswordField passwordField;

    /**
     * The wrapping controller for a card list
     *
     * @param server the server functions
     * @param mainCtrl the main controller
     * @param state the state of the user (admin or not)
     */
    @Inject
    public AdminPasswordCtrl(ServerUtils server, MainCtrl mainCtrl, UserState state) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.state = state;
    }

    /**
     * Initialisation method initialising FXML objects
     */
    @FXML
    public void initialize() {

    }

    /**
     * Checks the password in the passwordField when the "Enter" button is pressed
     */
    public void checkPassword() {
        try {
            final String password = this.passwordField.getText();
            // If no throw then password correct and continue
            this.server.getAllBoards(password);
            this.state.setPassword(password);

            passwordField.setText("");
            this.mainCtrl.closeAdminPassword();
            this.mainCtrl.showHistoryAdmin();
        } catch (final Exception e) {
            e.printStackTrace();
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect Password");
            alert.show();
        }
    }
}
