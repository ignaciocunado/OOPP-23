package client.scenes;

import client.MyFXML;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;

public class ListWrapper {
    private CardWrapper cardWrapper;
    @FXML
    private HBox hbox;
    private final HashSet<Integer> ids = new HashSet<>();
    private Board currentBoard;

    /**
     * Initiates the ListWrapper class
     * @param hbox the HBox to display the List of Cards in
     * @param currentBoard the Board the List of Cards belongs to
     */
    public ListWrapper(HBox hbox, Board currentBoard) {
        cardWrapper = new CardWrapper();
        this.hbox = hbox;
        this.currentBoard = currentBoard;
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
        currentBoard.addList(currentList);
        setListMethods(listPane, vbox, currentList, scrollPane);
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
                        cardWrapper.addCard(vbox, currentList);
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
                cardWrapper.removeExistingCard();
                cardWrapper.addExistingCard(vbox, cardWrapper.getPaneBeingDragged(), cardsAbove,
                        event.getSceneY(), listToDropInto);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
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
     * Refreshes the title of a List
     * @param selectedList the selected CardList
     * @param selectedText the TextField associated to the List
     */
    public void refreshListTitle(CardList selectedList, TextField selectedText) {
        selectedList.setTitle(selectedText.getText());
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
