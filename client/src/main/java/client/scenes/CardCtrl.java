package client.scenes;

import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.Tag;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public final class CardCtrl {

    private WebsocketUtils websocket;
    private ServerUtils server;
    private MainCtrl mainCtrl;

    private CardListCtrl cardListCtrl;

    @FXML
    private Pane cardPane;
    @FXML
    private TextField cardTitle;
    @FXML
    private Label cardDescription;
    @FXML
    private HBox tagList;
    private BoardOverviewCtrl boardOverviewCtrl;
    @FXML
    private ProgressBar progress;
    private Card card;
    @FXML
    private Pane insidePane;

    /**
     * The wrapping controller for a card
     *
     * @param websocket websocket setup
     * @param server   the server functions
     * @param mainCtrl
     */
    @Inject
    public CardCtrl(final WebsocketUtils websocket,
                    final ServerUtils server, final MainCtrl mainCtrl) {
        this.websocket = websocket;
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Additional setter to inject the correct card list controller
     *
     * @param cardListCtrl the card list controller
     */
    public void setCardListCtrl(final CardListCtrl cardListCtrl) {
        this.cardListCtrl = cardListCtrl;
    }

    /**
     * Gets the cardList controller to which this card us associated
     * @return the CardListCtrl
     */
    public CardListCtrl getCardListCtrl() {
        return this.cardListCtrl;
    }

    /**
     * Sets the controller of the BoardOverview
     * @param boardOverviewCtrl the controller
     */
    public void setBoardOverviewCtrl(final BoardOverviewCtrl boardOverviewCtrl) {
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.boardOverviewCtrl.setCardCtrl(this);
    }

    /**
     * Refreshes card with the card data provided
     *
     * @param card the data
     */
    public void refresh(Card card) {
        this.card = card;
        this.cardTitle.setText(this.card.getTitle());
        this.cardDescription.setText(this.card.getDescription());
        tagList.getChildren().clear();
        try {
            for (Tag tag : card.getTags()) {
                FXMLLoader loader = new FXMLLoader();
                Pane tagPane = loader.load(getClass().getResource("TagInBoardOverview.fxml")
                        .openStream());
                TagInBoardCtrl ctrl = loader.getController();
                ctrl.update(tag);
                tagList.getChildren().add(tagPane);
            }
        }
        catch (IOException e) {

        }
        insidePane.setStyle("-fx-background-color: " + card.getColour() + ";" +
                " -fx-background-radius: 10");
        cardPane.setStyle("-fx-background-color: " + cardListCtrl.getListBackgroundColour());
        progress.setProgress(this.card.calculateProgress());
    }

    /**
     * Gets the card this card ctrl is controller
     * @return the card
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Initialisation method initialising FXML objects
     */
    @FXML
    public void initialize() {
        cardPane.setOnDragDetected(event -> {
            final Dragboard db = cardPane.startDragAndDrop(TransferMode.MOVE);
            final ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(this.card.getId()));
            db.setContent(content);
            event.consume();
        });
        this.websocket.addCardListener(changedCard -> {
            if (changedCard == null || changedCard.getId() != (card.getId())) return;
            Platform.runLater(() -> this.refresh(changedCard));
        });
        cardTitle.focusedProperty().addListener(this::titleOrDescriptionEdited);
    }

    /**
     * Handler to delete a card
     */
    @FXML
    public void handleDeleteCard() {
        this.cardListCtrl.removeCard(this.card.getId());
    }

    /**
     * Handler to edit a card
     * @param event event for the mouse click
     */
    @FXML
    public void handleEditCard(final MouseEvent event) {
        if (event.getClickCount() != 2) return;
        this.mainCtrl.showCardEditor(this);
    }

    /**
     * getter for board
     * @return board
     */
    public Board getBoard() {
        return cardListCtrl.getBoard();
    }

    /**
     * Sets appropriate visual feedback when hovering over a card
     */
    public void mouseHover() {
        this.boardOverviewCtrl.setHoverCard(this.getCardPane());
        cardPane.setOpacity(0.70);
        this.insidePane = (Pane) cardPane.getChildren().get(0);
        if(card.getColour().equals("")) {
            insidePane.setStyle("-fx-background-color: #123456;-fx-background-radius:10 ;" +
                    " -fx-border-color: rgb(1,35,69); -fx-border-width: 3px");
        }
        else {
            insidePane.setStyle("-fx-background-color: " + card.getColour() +
                    ";-fx-background-radius:10 ; -fx-border-color: rgb(1,35,69);" +
                    " -fx-border-width: 3px");
        }
    }

    /**
     * Stops visual feedback for hovering over a card
     */
    public void mouseStopHover() {
        cardPane.setOpacity(1);
        if(this.card.getColour().equals("")) {
            insidePane.setStyle("-fx-background-color: #123456; " +
                    "-fx-background-radius: 10; -fx-border-width: 0px");
        }
        else {
            insidePane.setStyle("-fx-background-color: " + card.getColour() +
                    ";-fx-background-radius: 10; -fx-border-width: 0px");
        }
    }

    /**
     * Sets the cardPane
     * @param cardPane the Pane being assigned to it
     */
    public void setCardPane(Pane cardPane) {
        this.cardPane = cardPane;
    }

    /**
     * Gets the cardPane
     * @return the Pane of the current card
     */
    public Pane getCardPane() {
        return this.cardPane;
    }

    /**
     * Checks whether any of the fields lost focus and calls the editCard endpoint
     * @param observable lame ass parameter
     */
    public void titleOrDescriptionEdited(Observable observable) {
        if (!(observable instanceof ReadOnlyBooleanProperty)) return; // Doesn't happen
        final ReadOnlyBooleanProperty focused = (ReadOnlyBooleanProperty) observable;
        if (focused.getValue()) return; // If focuses then don't save yet
        this.server.editCard(this.card.getId(), cardTitle.getText(), cardDescription.getText(),
                this.card.getColour());
    }

    /**
     * A method that sets a new card
     * @param card a new card
     */
    public void setCard (Card card) {
        this.card = card;
    }
}
