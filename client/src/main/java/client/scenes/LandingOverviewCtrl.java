/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.Config;
import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.entities.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class LandingOverviewCtrl implements Initializable {

    private static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Config config;
    @FXML
    private Pane newPane;
    @FXML
    private TextField joinKey;
    @FXML
    private PasswordField joinPassword;
    @FXML
    private TextField createName;
    @FXML
    private PasswordField createPassword;
    @FXML
    private PasswordField createConfirmPassword;

    /**
     * Constructor to inject necessary classes into the controller
     * @param server
     * @param mainCtrl
     * @param config
     */
    @Inject
    public LandingOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
    }

    /**
     * Initialisation method initialising FXML objects
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
        this.server.setServer("http://localhost:8080/"); // TODO: temporary
    }

    /**
     * Swaps the current view to the boards
     * and loads it with the board's information
     */
    public void joinBoard() {
        final Board board = this.server.getBoard(this.joinKey.getText());
        if (board == null) {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("This board wasn't recognised");
            alert.show();
            return;
        }
        config.addBoard(this.joinKey.getText(),this.server.getServer());
        this.mainCtrl.showBoardOverview(board);
    }

    /**
     * Swaps the current view to the boards
     * creates that board, and loads that
     * board's information
     */
    public void createBoard() {
        if (!this.createPassword.getText().equals(this.createConfirmPassword.getText())) {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("The passwords don't match");
            alert.show();
            return;
        }

        final Board board = this.server.createBoard(this.createName.getText(),
                this.createPassword.getText());
        final ButtonType copyButton = new ButtonType("Copy", ButtonBar.ButtonData.OK_DONE);
        final ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        final Alert creationAlert = new Alert(Alert.AlertType.INFORMATION,
                "", copyButton, closeButton);
        creationAlert.setTitle("New Board Created");
        creationAlert.setHeaderText("You've created a new board with key: " + board.getKey() +
                " and with name: " + board.getName());
        final Optional<ButtonType> res = creationAlert.showAndWait();
        if (res.orElse(closeButton) == copyButton) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(board.getKey()), null);
        }
        config.addBoard(board.getKey(), this.server.getServer());
        this.mainCtrl.showBoardOverview(board);
    }

    /**
     * Stub method for refreshing boards
     */
    public void refresh() {}

    /**
     * Method to close the app
     */
    public void closeApp() {
        mainCtrl.closeApp();
    }

    /**
     * EventHandler for the button of the board history overview
     */
    public void openHistory(){
        this.mainCtrl.showHistory();
    }
}