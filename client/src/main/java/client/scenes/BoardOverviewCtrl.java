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
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.CardList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final CardWrapper cardWrapper;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox lists;
    private Board currentBoard;


    /**
     * Constructor to inject necessary classes into the controller
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.cardWrapper = new CardWrapper();
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        refresh(new Board("", ""));
    }


    /**
     * Stub method for refreshing the Board
     *
     * @param currentBoard the current Board being displayed
     */
    public void refresh(Board currentBoard) {
        this.currentBoard = currentBoard;
        this.lists.getChildren().clear();
        for (final CardList list : this.currentBoard.getLists()) {
            var pair = Main.FXML.load(CardListCtrl.class, "client", "scenes", "ListTemplate.fxml");
            this.lists.getChildren().add(pair.getValue());
            final CardListCtrl ctrl = pair.getKey();
            ctrl.setCardList(list);
            ctrl.refresh();
        }
    }

    /**
     * Adds a List to the current Board object and displays it
     */
    public void addList() throws IOException {
        final Board board = this.server.createList(this.currentBoard.getId(), "New List");
        this.refresh(board);
    }

    /**
     * Removes a list from the board based on an id
     * @param id the list id
     */
    public void removeListById(int id) {
        final Board board = this.server.deleteList(this.currentBoard.getId(), id);
        this.refresh(board);
    }
}