package client.scenes;

import client.MyFXML;
import commons.entities.Board;
import commons.entities.CardList;
import javafx.fxml.FXMLLoader;
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
    private final HBox lists;
    private final HashSet<Integer> ids = new HashSet<>();
    private Board currentBoard;

    private MainCtrl mainCtrl;

    /**
     * Initiates the ListWrapper class
     * @param lists the HBox to display the List of Cards in
     * @param currentBoard the Board the List of Cards belongs to
     * @param mainCtrl mainCtrl
     */
    public ListWrapper(HBox hbox, Board currentBoard, MainCtrl mainCtrl) {
        cardWrapper = new CardWrapper(mainCtrl);
        this.hbox = hbox;
        this.currentBoard = currentBoard;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Adds a List to the current Board object and displays it
     */
    public void addList() throws IOException {
        Pane listPane = FXMLLoader.load(getLocation("client", "scenes", "ListTemplate.fxml"));
        lists.getChildren().add(listPane);
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
        final Pane header = (Pane) listPane.getChildren().get(1);
        vbox.getChildren().get(0).setOnMouseClicked(event-> {
            try {cardWrapper.addCard(vbox, currentList);} catch (IOException e) {}
        });

        final TextField titleField = (TextField) header.getChildren().get(0);
        titleField.setText("Title: " + listPane.getId());
        refreshListTitle(currentList, titleField);
        titleField.setOnKeyReleased(event -> refreshListTitle(currentList, titleField));
        header.getChildren().get(1).setOnMouseClicked(event->
                removeList(listPane, currentList));

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
        lists.getChildren().remove(paneToBeRemoved);
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
