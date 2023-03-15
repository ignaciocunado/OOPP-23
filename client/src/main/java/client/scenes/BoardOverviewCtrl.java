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
    private Pane cardToDrag;
    private VBox vboxToRemove;
    private Card selectedCard;
    private CardList selectedList;


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
        ScrollPane scrollPane = new ScrollPane();
        VBox vbox = new VBox();
        for (int i = 0; i < listPane.getChildren().size(); i++) {
            if (listPane.getChildren().get(i).getClass() == Pane.class) {
                Pane cardPane = (Pane) listPane.getChildren().get(i);
                scrollPane = (ScrollPane) cardPane.getChildren().get(0);
                vbox = (VBox) scrollPane.getContent();
                VBox finalVbox = vbox;
                vbox.setSpacing(5);
                vbox.getChildren().get(0).setOnMouseClicked(event-> {
                    try {
                        addCard(finalVbox, currentList);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if (listPane.getChildren().get(i).getClass() == Button.class) {
                listPane.getChildren().get(i).setOnMouseClicked(event->
                    removeList(listPane, currentList.getId()));
            }
            if (listPane.getChildren().get(i).getClass() == TextField.class) {
                TextField title = (TextField) listPane.getChildren().get(i);
                title.setText("Title: " + listPane.getId());
                refreshTitle(currentList, title);
                title.setOnKeyReleased(event -> refreshTitle(currentList, title));
            }
        }
        VBox finalVbox1 = vbox;
        setMouseDragActions(listPane, currentList, scrollPane, finalVbox1);
        currentBoard.addList(currentList);
    }

    /**
     * Sets the necessary actions when dragging a Card over a List
     * @param listPane the Pane which the mouse is over
     * @param currentList the CardList corresponding to the Pane
     * @param scrollPane the ScrollPane contained in the listPane
     * @param vbox the VBox to which the Card will be added
     */
    private void setMouseDragActions(Pane listPane, CardList currentList, ScrollPane scrollPane,
                           VBox vbox) {
        listPane.setOnDragOver(event -> {
            if (event.getGestureSource() != listPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        listPane.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.setDropCompleted(true);
                removeExistingCard(vboxToRemove, cardToDrag);
                double scrollValue = scrollPane.getVvalue() *
                    (scrollPane.getContent().getBoundsInLocal().getHeight()
                    - scrollPane.getViewportBounds().getHeight());
                addExistingCard(vbox, cardToDrag, scrollValue,
                    event.getSceneY(), currentList);
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
        Pane cardPane = FXMLLoader.load(getLocation("client", "scenes", "CardTemplate.fxml"));
        vbox.getChildren().add(vbox.getChildren().size() - 1, cardPane);
        int counter = 1;
        while (cardsIds.contains(counter)) {
            counter++;
        }
        cardsIds.add(counter);
        cardPane.setId(String.valueOf(counter));
        Card currentCard = new Card();
        Pane taskPane = (Pane) cardPane.getChildren().get(0);
        taskPane.setOnDragDetected(event -> {
            Dragboard db = taskPane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("Pane source text");
            db.setContent(content);
            cardToDrag = cardPane;
            vboxToRemove = vbox;
            selectedCard = currentCard;
            selectedList = currentList;
            event.consume();
        });
        if (taskPane.getChildren().get(4).getClass() == Button.class) {
            taskPane.getChildren().get(4).setOnMouseClicked(event ->
                removeCard(cardPane, currentCard.getId(), vbox, currentList));
        }
        if (taskPane.getChildren().get(1).getClass() == TextField.class) {
            TextField cardTitle = (TextField) taskPane.getChildren().get(1);
            cardTitle.setText("Card: " + cardPane.getId());
            refreshCardTitle(currentCard, cardTitle);
            cardTitle.setOnKeyReleased(event -> refreshCardTitle(currentCard, cardTitle));
        }
        if (taskPane.getChildren().get(3).getClass() == TextField.class) {
            TextField cardTitle = (TextField) taskPane.getChildren().get(3);
            cardTitle.setText("Description: ");
            refreshCardDescription(currentCard, cardTitle);
            cardTitle.setOnKeyReleased(event -> refreshCardDescription(currentCard, cardTitle));
        }
        currentList.addCard(currentCard);
    }

    /**
     * Removes an existing List from the Board
     * @param toBeRemoved the Pane which needs to be deleted
     * @param idOfList the ID of the List which needs to be deleted
     */
    public void removeList(Pane toBeRemoved, int idOfList) {
        ids.remove(toBeRemoved.getId());
        hbox.getChildren().remove(toBeRemoved);
        for (CardList list : currentBoard.getListsOnBoard()) {
            if (list.getId() == idOfList) {
                currentBoard.removeList(list);
                break;
            }
        }
    }

    /**
     * Refreshes the title of a List
     * @param selectedList the selected CardList
     * @param selectedText the TextField associated to the List
     */
    public void refreshTitle(CardList selectedList, TextField selectedText) {
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
     * Adds an existing Card to a list
     * @param vbox the VBox associated to the List
     * @param draggingCard the Pane which is being dragged
     * @param scrollValue the relative position which the user is seeing
     * @param sceneY mouse horizontal position
     * @param currentList the List to which the Card will be added
     */
    public void addExistingCard(VBox vbox, Pane draggingCard, double scrollValue,
                                double sceneY, CardList currentList) {
        int relativePosition = (int) (scrollValue/184.20001220703125);
        int size = vbox.getChildren().size();
        if (size <= 1) {
            vbox.getChildren().add(0, draggingCard);
        }
        else if (sceneY < 300) {
            vbox.getChildren().add(relativePosition, draggingCard);
        }
        else if (sceneY < 550) {
            if (relativePosition + 1 == vbox.getChildren().size()) {
                vbox.getChildren().add(vbox.getChildren().size() - 1, draggingCard);
            }
            else {
                vbox.getChildren().add(relativePosition + 1, draggingCard);
            }
        }
        else {
            if (relativePosition + 2 == vbox.getChildren().size()) {
                vbox.getChildren().add(vbox.getChildren().size() - 1, draggingCard);
            }
            else {
                vbox.getChildren().add(relativePosition + 2, draggingCard);
            }
        }
        currentList.addCard(selectedCard);
        Pane taskPane = (Pane) draggingCard.getChildren().get(0);
        taskPane.setOnDragDetected(event -> {
            Dragboard db = taskPane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("Pane source text");
            db.setContent(content);
            cardToDrag = draggingCard;
            vboxToRemove = vbox;
            selectedList = currentList;
            event.consume();
        });
        taskPane.getChildren().get(4).setOnMouseClicked(event ->
            removeCard(draggingCard, selectedCard.getId(), vbox, selectedList));
    }

    /**
     * Removes an existing Card from a list
     * @param vbox the VBox associated to the List
     * @param cardToDrag the Pane which is being dragged
     */
    public void removeExistingCard(VBox vbox, Pane cardToDrag) {
        vbox.getChildren().remove(cardToDrag);
        selectedList.getCards().remove(selectedCard);
    }

    /**
     * Removes an existing Card from the List
     * @param toBeRemoved the Pane which needs to be deleted
     * @param idOfCard the ID of the Card which needs to be deleted
     * @param vbox the VBox associated to the List
     * @param currentList the current CardList
     */
    public void removeCard(Pane toBeRemoved,int idOfCard, VBox vbox, CardList currentList){
        cardsIds.remove(toBeRemoved.getId());
        vbox.getChildren().remove(toBeRemoved);
        for (Card card : currentList.getCards()){
            if(card.getId() == idOfCard){
                currentList.removeCard(card);
                break;
            }
        }
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