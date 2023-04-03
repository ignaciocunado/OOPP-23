package client.utils;

import com.google.inject.Singleton;
import commons.entities.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Singleton
public final class WebsocketUtils {

    private StompSession stompSession;
    private List<Consumer<Board>> boardListeners;
    private List<Consumer<CardList>> cardListListeners;
    private List<Consumer<Card>> cardListeners;
    private List<Consumer<Tag>> tagListeners;
    private List<Consumer<Task>> taskListeners;
    /**
     * Constructs a new WebsocketUtils instance with empty listener lists.
     */
    public WebsocketUtils() {
        this.boardListeners = new ArrayList<>();
        this.cardListListeners = new ArrayList<>();
        this.cardListeners = new ArrayList<>();
        this.tagListeners = new ArrayList<>();
        this.taskListeners = new ArrayList<>();
    }
    /**
     * Adds a listener for updates on board entities.
     *
     * @param boardListener The listener to be added.
     */
    public void addBoardListener(final Consumer<Board> boardListener) {
        this.boardListeners.add(boardListener);
    }
    /**
     * Adds a listener for updates on card list entities.
     *
     * @param cardListConsumer The listener to be added.
     */
    public void addCardListListener(final Consumer<CardList> cardListConsumer) {
        this.cardListListeners.add(cardListConsumer);
    }
    /**
     * Adds a listener for updates on card entities.
     *
     * @param cardConsumer The listener to be added.
     */
    public void addCardListener(final Consumer<Card> cardConsumer) {
        this.cardListeners.add(cardConsumer);
    }
    /**
     * Adds a listener for updates on tag entities.
     *
     * @param tagConsumer The listener to be added.
     */
    public void addTagListener(final Consumer<Tag> tagConsumer) {
        this.tagListeners.add(tagConsumer);
    }
    /**
     * Adds a listener for updates on task entities.
     *
     * @param taskConsumer The listener to be added.
     */
    public void addTaskListener(final Consumer<Task> taskConsumer) {
        this.taskListeners.add(taskConsumer);
    }

    /**
     * Connects to a websocket endpoint using the STOMP protocol.
     *
     * @param connectionUri The URI of the endpoint to connect to.
     */
    public void connect(final String connectionUri) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            this.stompSession = stomp.connect(connectionUri, new StompSessionHandlerAdapter() {
            }).get();
            registerForUpdates("/topic/board", Board.class, board -> {
                this.boardListeners.forEach(listener -> listener.accept(board));
            });
            registerForUpdates("/topic/cardlist", CardList.class, cardList -> {
                this.cardListListeners.forEach(listener -> listener.accept(cardList));
            });
            registerForUpdates("/topic/card", Card.class, card -> {
                this.cardListeners.forEach(listener -> listener.accept(card));
            });
            registerForUpdates("/topic/tag", Tag.class, tag -> {
                this.tagListeners.forEach(listener -> listener.accept(tag));
            });
            registerForUpdates("/topic/task", Task.class, task -> {
                this.taskListeners.forEach(listener -> listener.accept(task));
            });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Disconnects from the websocket endpoint, if connected.
     */
    public void disconnect () {
        if (this.stompSession != null) this.stompSession.disconnect();
    }

    /**
     * Registers a subscription for updates on a specified destination
     * using the STOMP protocol.The subscription is registered with a consumer
     * that will handle the updates received from the destination.
     *
     * @param dest     The destination to subscribe to.
     * @param type     The expected class type of the payload received from the destination.
     * @param consumer The consumer that will handle the updates received from the destination.
     * @param <T>      The type of the payload received from the destination.
     */
    private <T> void registerForUpdates (String dest, Class<T> type, Consumer<T> consumer) {
        stompSession.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType (StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame (StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }

        });
    }

}
