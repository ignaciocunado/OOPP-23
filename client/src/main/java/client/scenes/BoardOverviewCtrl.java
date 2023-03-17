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

import client.MyFXML;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox hbox;
    private final HashSet<Integer> ids = new HashSet<>();
    private final HashSet<Integer> cardsIds = new HashSet<>();
    private Board currentBoard;
    private Pane paneBeingDragged;
    private VBox originalVBox;
    private Card cardBeingDragged;
    private CardList originalCardList;


    /**
     * Constructor to inject necessary classes into the controller
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(20,20,20,0));
        refresh(new Board("",""));
    }


    /**
     * Stub method for refreshing the Board
     * @param currentBoard the current Board being displayed
     */
    public void refresh(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     * Method to close the app
     */
    public void closeApp() {
        mainCtrl.closeApp();
    }

    /**
     * Method to minimize the app
     */
    public void minimizeApp() {
        mainCtrl.minimizeWindow();
    }

    /**
     * Adds a List to the current Board object and displays it
     */
    public void addList() throws IOException {
        Pane listPane = FXMLLoader.load(getLocation("client", "scenes", "ListTemplate.fxml"));
        hbox.getChildren().add(listPane);
        int counter = 1;
        while(ids.contains(counter)){
            counter++;
        }
        ids.add(counter);
        listPane.setId(String.valueOf(counter));
        CardList currentList = new CardList("");
        Pane cardPane = (Pane) listPane.getChildren().get(0);
        ScrollPane scrollPane = (ScrollPane) cardPane.getChildren().get(0);
        VBox vbox = (VBox) scrollPane.getContent();
        vbox.setSpacing(5);
        setListMethods(listPane, vbox, currentList, scrollPane);
        currentBoard.addList(currentList);
    }

    /**
     * Sets the methods for actions on elements of the added List
     * @param listPane the new List being added
     * @param vbox the VBox which is a sub-sub-child of the listPane
     * @param currentList the new CardList corresponding to the listPane
     * @param scrollPane the parent of vbox
     */
    private void setListMethods(Pane listPane, VBox vbox,
                                CardList currentList, ScrollPane scrollPane) {
        for (int i = 0; i < listPane.getChildren().size(); i++) {
            if (listPane.getChildren().get(i).getClass() == Pane.class) {
                vbox.getChildren().get(0).setOnMouseClicked(event-> {
                    try {
                        addCard(vbox, currentList);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if (listPane.getChildren().get(i).getClass() == Button.class) {
                listPane.getChildren().get(i).setOnMouseClicked(event->
                    removeList(listPane, currentList));
            }
            if (listPane.getChildren().get(i).getClass() == TextField.class) {
                TextField title = (TextField) listPane.getChildren().get(i);
                title.setText("Title: " + listPane.getId());
                refreshListTitle(currentList, title);
                title.setOnKeyReleased(event -> refreshListTitle(currentList, title));
            }
        }
        setDropCardOnListActions(listPane, currentList, scrollPane, vbox);
    }

    /**
     * Sets the necessary actions when dragging a Card over a List (part of a List's methods)
     * @param paneToDropInto the Pane which the Card is hovering over
     * @param listToDropInto the CardList corresponding to the Pane
     * @param scrollPane the ScrollPane contained in the paneToDropInto
     * @param vbox the VBox to which the Card will be added
     */
    private void setDropCardOnListActions(Pane paneToDropInto, CardList listToDropInto,
                                          ScrollPane scrollPane, VBox vbox) {
        int cardsAbove = (int) (scrollPane.getVvalue() *
            (scrollPane.getContent().getBoundsInLocal().getHeight()
                - scrollPane.getViewportBounds().getHeight()) / 184.20001220703125);

        paneToDropInto.setOnDragOver(event -> {
            if (event.getGestureSource() != paneToDropInto && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        paneToDropInto.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.setDropCompleted(true);
                removeExistingCard();
                addExistingCard(vbox, paneBeingDragged, cardsAbove,
                    event.getSceneY(), listToDropInto);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    /**
     * Adds a Card to the current List object and displays it
     * @param vbox the VBox associated to the List
     * @param currentList the current CardList
     */
    public void addCard(VBox vbox, CardList currentList) throws IOException {
        Pane outerCardPane = FXMLLoader.load(getLocation("client", "scenes", "CardTemplate.fxml"));
        vbox.getChildren().add(vbox.getChildren().size() - 1, outerCardPane);
        int counter = 1;
        while (cardsIds.contains(counter)) {
            counter++;
        }
        cardsIds.add(counter);
        outerCardPane.setId(String.valueOf(counter));
        Card newCard = new Card();
        setCardMethods(vbox, currentList, outerCardPane, newCard);
        currentList.addCard(newCard);
    }

    /**
     * Adds an existing Card to a list
     * @param vbox the VBox associated to the List
     * @param draggingCard the Pane which is being dragged
     * @param cardsAbove the number of Cards above what the user can see
     * @param sceneY mouse horizontal position
     * @param currentList the List to which the Card will be added
     */
    public void addExistingCard(VBox vbox, Pane draggingCard, int cardsAbove,
                                double sceneY, CardList currentList) {
        if (cardsAbove + 1 >= vbox.getChildren().size()){
            vbox.getChildren().add(vbox.getChildren().size() - 1, draggingCard);
        }
        else if (sceneY < 300) {
            vbox.getChildren().add(cardsAbove, draggingCard);
        }
        else if (sceneY < 550) {
            vbox.getChildren().add(cardsAbove + 1, draggingCard);
        }
        else {
            vbox.getChildren().add(cardsAbove + 2, draggingCard);
        }
        setCardMethods(vbox, currentList, draggingCard, cardBeingDragged);
    }

    /**
     * Sets the methods of a new Card which has just been created
     * @param vbox the VBox containing the newly added Card
     * @param currentList the CardList to which the Card now belongs to
     * @param outerCardPane the Pane of the Card
     * @param newCard the Card which has just been added
     */
    public void setCardMethods(VBox vbox, CardList currentList, Pane outerCardPane, Card newCard) {
        Pane innerCardPane = (Pane) outerCardPane.getChildren().get(0);
        outerCardPane.setOnDragDetected(event -> {
            Dragboard db = innerCardPane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("Pane source text");
            db.setContent(content);
            paneBeingDragged = outerCardPane;
            originalVBox = vbox;
            cardBeingDragged = newCard;
            originalCardList = currentList;
            event.consume();
        });

        innerCardPane.getChildren().get(4).setOnMouseClicked(event ->
            removeCard(outerCardPane, newCard, vbox, currentList));

        TextField cardTitle = (TextField) innerCardPane.getChildren().get(1);
        cardTitle.setText("Card: " + outerCardPane.getId());
        refreshCardTitle(newCard, cardTitle);
        cardTitle.setOnKeyReleased(event -> refreshCardTitle(newCard, cardTitle));

        TextField cardDescription = (TextField) innerCardPane.getChildren().get(3);
        cardDescription.setText("Description: ");
        refreshCardDescription(newCard, cardTitle);
        cardTitle.setOnKeyReleased(event -> refreshCardDescription(newCard, cardDescription));
    }


    /**
     * Removes an existing List from the Board
     * @param paneToBeRemoved the Pane which needs to be deleted
     * @param listToBeRemoved the List which needs to be deleted
     */
    public void removeList(Pane paneToBeRemoved, CardList listToBeRemoved) {
        ids.remove(paneToBeRemoved.getId());
        hbox.getChildren().remove(paneToBeRemoved);
        currentBoard.removeList(listToBeRemoved);
    }

    /**
     * Removes an existing Card from the List
     * @param paneToBeRemoved the Pane which needs to be deleted
     * @param cardToBeRemoved the Card which needs to be deleted
     * @param vbox the VBox associated to the List
     * @param currentList the current CardList
     */
    public void removeCard(Pane paneToBeRemoved, Card cardToBeRemoved,
                           VBox vbox, CardList currentList){
        cardsIds.remove(paneToBeRemoved.getId());
        vbox.getChildren().remove(paneToBeRemoved);
        currentList.removeCard(cardToBeRemoved);
    }

    /**
     * Removes an existing Card from a List
     */
    public void removeExistingCard() {
        originalVBox.getChildren().remove(paneBeingDragged);
        originalCardList.getCards().remove(cardBeingDragged);
    }

    /**
     * Refreshes the title of a List
     * @param selectedList the selected CardList
     * @param selectedText the TextField associated to the List
     */
    public void refreshListTitle(CardList selectedList, TextField selectedText) {
        selectedList.setTitle(selectedText.getText());
    }

    /**
     * Refreshes the title of a Card
     * @param selectedCard the selected Card
     * @param selectedText the TextField associated to the Card
     */
    public void refreshCardTitle(Card selectedCard, TextField selectedText){
        selectedCard.setTitle(selectedText.getText());
    }

    /**
     * Refreshes the description of a Card
     * @param selectedCard the selected Card
     * @param selectedText the TextField associated to the Card
     */
    public void refreshCardDescription(Card selectedCard, TextField selectedText){
        selectedCard.setDescription(selectedText.getText());
    }

    /**
     * Gets the location of a resource with the given String elements
     * @param parts Strings of where to find the resource
     * @return the URL of the requested resource
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }


}