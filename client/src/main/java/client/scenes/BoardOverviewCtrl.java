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
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final WebsocketUtils websocket;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField title;
    @FXML
    private HBox lists;
    @FXML
    private BorderPane mainPane;
    @FXML
    private ScrollPane scroller;
    private Board currentBoard;
    private Pane hoverCardPane = null;
    private CardCtrl cardCtrl;


    /**
     * The wrapping controller for a card list
     *
     * @param websocket websocket setup
     * @param server the server functions
     * @param mainCtrl the main controller
     */
    @Inject
    public BoardOverviewCtrl(WebsocketUtils websocket, ServerUtils server,
                             MainCtrl mainCtrl) {
        this.websocket = websocket;
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
        refresh(new Board("","", ""));
        scroller.setFocusTraversable(false);
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
        this.setTyped();
        this.setPressed();
        this.title.setText(this.currentBoard.getName());
        this.lists.getChildren().clear();
        for (final CardList list : this.currentBoard.getLists()) {
            var pair = Main.FXML.load(CardListCtrl.class, "client", "scenes", "ListTemplate.fxml");
            this.lists.getChildren().add(pair.getValue());
            final CardListCtrl ctrl = pair.getKey();
            ctrl.refresh(list);
        }
        setRightColours();
    }

    /**
     * Sets the shortcuts for the "typed" characters such as 'e' and '?'
     */
    public void setTyped() {
        mainPane.setOnKeyTyped(event -> {
            if (event.getCharacter().equals("?")) {
                this.mainCtrl.showShortcuts();
            }
            else if (event.getCharacter().equals("e")) {
                Pane temp = (Pane) hoverCardPane.getChildren().get(0);
                TextField field = (TextField) temp.getChildren().get(3);
                field.requestFocus();
                field.setOnKeyPressed(event1 -> {
                    if (event1.getCode().equals(KeyCode.CONTROL)) {
                        this.getCardCtrl().titleOrDescriptionEdited(
                            field.focusTraversableProperty());
                        mainPane.requestFocus();
                    }
                });
            }
            else if (event.getCharacter().equals("t")) {
                this.mainCtrl.showTagOverview();
            }
            else if (event.getCharacter().equals("c")) {
                this.mainCtrl.showBoardSettings(currentBoard);
            }
        });
    }

    /**
     * Sets the shortcuts for the keyboard keys being pressed (UP, DOWN, etc.)
     */
    public void setPressed() {
        mainPane.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.F1)) {
                startShortcuts();
            }
            else if (ke.getCode().equals(KeyCode.ESCAPE)) {
                cardCtrl.mouseStopHover();
                mainPane.setMouseTransparent(false);
                mainPane.getScene().setCursor(Cursor.DEFAULT);
            }
            else if (ke.getCode().equals(KeyCode.ENTER)) {
                mainCtrl.showCardEditor(this.cardCtrl);
            }
            else if (ke.getCode().equals(KeyCode.BACK_SPACE) ||
                ke.getCode().equals(KeyCode.DELETE)) {
                deleteCard();
            }
            else {
                setArrows(ke);
            }
        });
    }

    /**
     * Sets the keyboard shortcuts for moving the hover with the arrow keys
     * @param ke the keyEvent passed on from setPressed()q
     */
    public void setArrows(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.DOWN)) {
            moveDown();
        } else if (ke.getCode().equals(KeyCode.UP)) {
            moveUp();
        }
    }

    /**
     * Starts the shortcuts with F1, so that UP, DOWN, etc. can be used
     */
    public void startShortcuts() {
        if (getHoverCardPane() != null) {
            mainPane.requestFocus();
            mainPane.setMouseTransparent(true);
            mainPane.getScene().setCursor(Cursor.NONE);
            cardCtrl.setCardPane(this.getHoverCardPane());
        }
        mainPane.setOnKeyReleased(ke -> {
            if (ke.getCode().equals(KeyCode.F1)) {
                cardCtrl.mouseHover();
            }
        });
    }

    /**
     * Moves the highlight of cards one place up in the same list
     */
    public void moveUp() {
        if (getHoverCardPane() != null) {
            VBox parentList = (VBox) getHoverCardPane().getParent();
            int index = parentList.getChildren().indexOf(getHoverCardPane());
            if (index > 0) {
                Pane nextPane = (Pane) parentList.getChildren().get(index - 1);
                cardCtrl.mouseStopHover();
                this.getCardCtrl().setCardPane(nextPane);
                cardCtrl.mouseHover();
            }
        }
    }

    /**
     * Moves the highlight of cards one place down in the same list
     */
    public void moveDown() {
        if (this.getHoverCardPane() != null) {
            VBox parentList = (VBox) getHoverCardPane().getParent();
            int index = parentList.getChildren().indexOf(getHoverCardPane());
            if (index < parentList.getChildren().size() - 2) {
                Pane nextPane = (Pane) parentList.getChildren().get(index + 1);
                cardCtrl.mouseStopHover();
                getCardCtrl().setCardPane(nextPane);
                cardCtrl.mouseHover();
            }
        }
    }

    /**
     * Deletes the highlighted card
     */
    public void deleteCard() {
        CardListCtrl cardListCtrl = this.getCardCtrl().getCardListCtrl();
        VBox parent = (VBox) this.getHoverCardPane().getParent();
        int index = parent.getChildren().indexOf(this.getHoverCardPane());
        Card card = cardListCtrl.getCardList().getCards().get(index);
        //TODO:this still does not work
//        cardListCtrl.removeCard(card.getId());
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
        this.refresh(this.server.getBoard(currentBoard.getKey()));
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

    /**
     * Sets the cardPane currently being hovered over
     * @param hoverCardPane the Pane being hovered over
     */
    public void setHoverCard(Pane hoverCardPane) {
        this.hoverCardPane = hoverCardPane;
    }

    /**
     * Gets the cardPane currently being hovered over
     * @return the cardPane currently being hovered over
     */
    public Pane getHoverCardPane() {
        return this.hoverCardPane;
    }

    /**
     * Sets the controller of the card being hovered over
     * @param cardCtrl the controller
     */
    public void setCardCtrl(CardCtrl cardCtrl) {
        this.cardCtrl = cardCtrl;
    }

    /**
     * Gets the controller of the card being hovered over
     * @return the controller of the card being hovered over
     */
    public CardCtrl getCardCtrl() {
        return this.cardCtrl;
    }

    /**
     * Method to call instead of refresh
     */
    public void setRightColours() {
        lists.setStyle("-fx-spacing: 20; -fx-background-color:" + currentBoard.getColour());
        mainPane.setStyle("-fx-background-color:" + currentBoard.getColour());
    }
}