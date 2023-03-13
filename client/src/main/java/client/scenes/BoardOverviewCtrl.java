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
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
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
    private HashSet<Integer> ids = new HashSet<>();
    private HashSet<Integer> cardsIds = new HashSet<>();
    private Board currentBoard;
    private CardList currentList;


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

    public void refreshList(CardList currentList){
        this.currentList = currentList;
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
        CardList currentList = new CardList();
        for (int i = 0; i < listPane.getChildren().size(); i++) {
            if (listPane.getChildren().get(i).getClass() == Pane.class) {
                Pane current = (Pane) listPane.getChildren().get(i);
                ScrollPane scrollPane = (ScrollPane) current.getChildren().get(0);
                current.getChildren().get(0).setOnMouseClicked(event-> {
                    try {
                        addCard(current);//addCard will be here
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            if (listPane.getChildren().get(i).getClass() == Button.class) {
                listPane.getChildren().get(i).setOnMouseClicked(event-> {
                    removeList(listPane, currentList.getId());
                });
            }
            if (listPane.getChildren().get(i).getClass() == TextField.class) {
                TextField title = (TextField) listPane.getChildren().get(i);
                title.setText("Title: "+listPane.getId());
                refreshTitle(currentList, title);
                title.setOnKeyReleased(event -> refreshTitle(currentList, title));
            }
        }
        currentBoard.addList(currentList);
    }

    public void addCard(Pane list) throws IOException {
        Pane listPane = FXMLLoader.load(getLocation("client", "scenes", "CardTemplate.fxml"));
        list.getChildren().add(listPane);
        VBox vbox = null;
        int counter = 1;
        while(cardsIds.contains(counter)){
            counter++;
        }
        cardsIds.add(counter);
        listPane.setId(String.valueOf(counter));
        Card currentCard = new Card();
        Pane cardPane = (Pane) listPane.getChildren().get(0);
        for (int i = 0; i < cardPane.getChildren().size(); i++) {
//            if (cardPane.getChildren().get(i).getClass() == Pane.class) {
//                Pane current = (Pane) cardPane.getChildren().get(i);
//                TextField tag = (TextField) current.getChildren().get(0);
//                tag.setText("Tag");
//                refreshTag(currentCard, tag);
//                tag.setOnKeyReleased(event -> refreshTag(currentCard, tag));
//            }
            if (listPane.getChildren().get(i).getClass() == Button.class && listPane.getChildren().get(i).getId().equals("closeButton")) {
                listPane.getChildren().get(i).setOnMouseClicked(event-> {
                    removeCard(cardPane, currentCard.getId(), vbox);
                });
            }
            if (listPane.getChildren().get(i).getClass() == TextField.class && listPane.getChildren().get(i).getId().equals("cardTitle")) {
                TextField cardTitle = (TextField) listPane.getChildren().get(i);
                cardTitle.setText("Title: "+listPane.getId());
                refreshCardTitle(currentCard, cardTitle);
                cardTitle.setOnKeyReleased(event -> refreshCardTitle(currentCard, cardTitle));
            }
        }
        currentList.addCard(currentCard);
    }
    /**
     * Refreshes the title of a List
     * @param selectedList the selected CardList
     * @param selectedText the TextFiled associated to the List
     */
    public void refreshTitle(CardList selectedList, TextField selectedText) {
        selectedList.setTitle(selectedText.getText());
    }

    public void refreshCardTitle(Card selectedCard, TextField selectedText){
        selectedCard.setTitle(selectedText.getText());
    }

    /**
     * Removes an existing List from the Board
     * @param toBeRemoved the Pane which needs to be deleted
     * @param idOfList the ID of the List which needs to be deleted
     */
    public void removeList(Pane toBeRemoved, int idOfList) {
        ids.remove(toBeRemoved.getId());
        int index = hbox.getChildren().indexOf(toBeRemoved);
        hbox.getChildren().remove(index);
        for (CardList list : currentBoard.getListsOnBoard()) {
            if (list.getId() == idOfList) {
                currentBoard.removeList(list);
                break;
            }
        }
    }

    public void removeCard(Pane toBeRemoved, int idOfCard, VBox vbox){
        ids.remove(toBeRemoved.getId());
        int index = vbox.getChildren().indexOf(toBeRemoved);
        vbox.getChildren().remove(index);
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