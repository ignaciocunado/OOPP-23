package client.scenes;

import com.google.inject.Inject;
import commons.entities.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Consumer;

public final class CardListCtrl {

    @FXML
    private Pane list;
    @FXML
    private TextField listNameField;
    @FXML
    private ScrollPane scroll;
    @FXML
    private VBox cards;

    private CardList cardList;
    private CardWrapper cardWrapper;
    private BoardOverviewCtrl ctrl;

    private Consumer<Integer> onDelete;


    /**
     * The wrapping renderable for a card list
     * @param wrapper a wrapper for card methods
     * @param ctrl the board controller
     */
    @Inject
    public CardListCtrl(final BoardOverviewCtrl ctrl, final CardWrapper wrapper) {
        this.cardWrapper = wrapper;
        this.ctrl = ctrl;
    }

    /**
     * Refreshes all data associated with a cardlist
     */
    public void refresh() {
        this.listNameField.setText(this.cardList.getTitle());
        this.setDropCardOnListActions(list, this.cardList, scroll, cards);
    }

    /**
     * Handles the add card button
     */
    public void handleAddCard() {
        try {cardWrapper.addCard(cards, this.cardList);} catch (IOException e) {}
    }

    /**
     * Handles the delete list button
     */
    public void handleDeleteList() {
        this.onDelete.accept(this.cardList.getId());
    }

    /**
     * Sets the on delete handler for logic about what to do after deleting
     * @param onDelete the callback to run
     */
    public void onDelete(final Consumer<Integer> onDelete) {
        this.onDelete = onDelete;
    }

    private void setupRemoveButton(final Pane header) {
        header.getChildren().get(1).setOnMouseClicked(event-> {
            ctrl.removeListById(this.cardList.getId());
        });
    }

    private void setupAddCardButton(final VBox cards) {
        cards.getChildren().get(0).setOnMouseClicked(event -> {
            try {cardWrapper.addCard(cards, this.cardList);} catch (IOException e) {}
        });
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
     * Sets the cardlist to get details from
     * @param cardList the cardlist
     */
    public void setCardList(final CardList cardList) {
        this.cardList = cardList;
    }



}
