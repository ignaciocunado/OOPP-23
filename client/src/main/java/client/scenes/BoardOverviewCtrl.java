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

import client.Main;
import client.config.Config;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.CardList;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final WebsocketUtils websocket;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Config config;

    @FXML
    private TextField title;
    @FXML
    private HBox lists;
    private Board currentBoard;


    /**
     * The wrapping controller for a card list
     *
     * @param websocket websocket setup
     * @param server the server functions
     * @param mainCtrl the main controller
     * @param config the config file
     */
    @Inject
    public BoardOverviewCtrl(WebsocketUtils websocket, ServerUtils server, MainCtrl mainCtrl, Config config) {
        this.websocket = websocket;
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        refresh(new Board("","", ""));
        title.focusedProperty().addListener(observable -> {
            if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
            final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
            if (focused.getValue()) return; // If focuses then don't save yet
            this.currentBoard.setName(this.title.getText());
            this.server.editBoard(this.currentBoard.getId(), this.currentBoard);
        });
        this.websocket.addBoardListener(board -> {
            if (board == null || !board.getKey().equals(this.currentBoard.getKey())) return;
            Platform.runLater(() -> this.refresh(board));
        });
    }

    /**
     * Refreshes the Board and the lists in it.
     *
     * @param currentBoard the current Board being displayed
     */
    public void refresh(Board currentBoard) {
        this.currentBoard = currentBoard;

        this.title.setText(this.currentBoard.getName());
        this.lists.getChildren().clear();
        for (final CardList list : this.currentBoard.getLists()) {
            var pair = Main.FXML.load(CardListCtrl.class, "client", "scenes", "ListTemplate.fxml");
            this.lists.getChildren().add(pair.getValue());
            final CardListCtrl ctrl = pair.getKey();
            ctrl.refresh(list);
        }
    }

    /**
     * Refreshes the Board and the lists in it by using the key
     * to get the board details.
     */
    public void refresh() {
        this.refresh(this.server.getBoard(this.currentBoard.getKey()));
    }

    /**
     * Adds a List to the current Board object and displays it
     */
    @FXML
    private void addList() {
        final Board board = this.server.createList(this.currentBoard.getId(), "New List");
        this.refresh(board);
    }

    /**
     * Shows the board settings
     */
    @FXML
    private void showBoardSettings() {
        this.mainCtrl.showBoardSettings(this.getBoard());
    }

    /**
     * Shows the board settings
     */
    @FXML
    private void showTagOverview() {
        this.mainCtrl.showTagOverview();

    }

    /**
     * Shows the main landing overview scene
     */
    @FXML
    private void showLandingOverview(){
        mainCtrl.showLandingOverview();
    }

    /**
     * Removes a list from the board based on an id
     * @param id the list id
     */
    public void removeListById(int id) {
        final Board board = this.server.deleteList(this.currentBoard.getId(), id);
        this.refresh(board);
    }

    /**
     * Getter for Board
     * @return board
     */
    public Board getBoard() {
        return this.currentBoard;
    }
}