package client.renderable;

import client.MyFXML;
import client.scenes.BoardOverviewCtrl;
import client.scenes.CardWrapper;
import commons.entities.CardList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class CardListRenderable {

    private CardWrapper cardWrapper;
    private final BoardOverviewCtrl ctrl;
    private final CardList list;


    public CardListRenderable(final CardWrapper wrapper, final BoardOverviewCtrl ctrl, final CardList list) {
        this.cardWrapper = wrapper;
        this.ctrl = ctrl;
        this.list = list;
    }

    public Node render() throws IOException {
        final Pane listPane = FXMLLoader.load(getLocation("client", "scenes", "ListTemplate.fxml"));
        listPane.setId("list-"+this.list.getId());

        final Pane header = (Pane) listPane.getChildren().get(1);
        final Pane cardPane = (Pane) listPane.getChildren().get(0);
        final ScrollPane scrollPane = (ScrollPane) cardPane.getChildren().get(0);
        final VBox cards = (VBox) scrollPane.getContent();

        this.setupTitle(header);
        this.setupRemoveButton(header);
        this.setupAddCardButton(cards);
        this.setDropCardOnListActions(listPane, this.list, scrollPane, cards);
        return listPane;
    }

    private void setupTitle(final Pane header) {
        final TextField titleField = (TextField) header.getChildren().get(0);
        titleField.setText(this.list.getTitle());
        titleField.setOnKeyReleased(event -> {
            this.list.setTitle(event.getText());
        });
    }

    private void setupRemoveButton(final Pane header) {
        header.getChildren().get(1).setOnMouseClicked(event-> {
            ctrl.removeListById(this.list.getId());
        });
    }

    private void setupAddCardButton(final VBox cards) {
        cards.getChildren().get(0).setOnMouseClicked(event -> {
            try {cardWrapper.addCard(cards, this.list);} catch (IOException e) {}
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
     * Gets the location of a resource with the given String elements
     * @param parts Strings of where to find the resource
     * @return the URL of the requested resource
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return MyFXML.class.getClassLoader().getResource(path);
    }
}
