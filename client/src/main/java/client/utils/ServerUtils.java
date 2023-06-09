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
package client.utils;

import com.google.inject.Singleton;
import commons.entities.*;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.http.HttpStatus;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Singleton
public class ServerUtils {

    private Client client;
    private String server;
    private ExecutorService exec;

    /**
     * Empty constructor
     */
    public ServerUtils() {
        final ClientConfig cc = new ClientConfig().connectorProvider(new ApacheConnectorProvider());
        this.client = ClientBuilder.newClient(cc);
    }

    /**
     * Object that has utility methods for accessing the server
     *
     * @param server the server string
     */
    public ServerUtils(final String server) {
        final ClientConfig cc = new ClientConfig().connectorProvider(new ApacheConnectorProvider());
        this.client = ClientBuilder.newClient(cc);
        this.server = server;
    }

    /**
     * Gets current server string
     *
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets current server string
     *
     * @param server the new server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Checks the existence of a server on the connection uri
     *
     * @param connectionUri the connectionUri to check the existence of
     * @return whether it exists
     */
    public boolean ping(final String connectionUri) {
        try {
            final WebTarget target = client.target(connectionUri + "/api/ping");
            final Invocation.Builder builder = target.request();
            builder.property(ClientProperties.CONNECT_TIMEOUT, 1000);
            builder.property(ClientProperties.READ_TIMEOUT, 1000);
            final Response response = builder.get();
            if (response.getStatus() != HttpStatus.OK.value()) return false;
            if (Objects.equals(response.getHeaderString("Server"), "Talio V1")) return true;
        } catch (final NotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * Creates a new board in the given server with the given password
     *
     * @param name     the name to create the board with
     * @param password the password to create the board with
     * @return the newly created board
     */
    public Board createBoard(final String name, final String password) {
        return client.target(this.server).path("/api/board")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(
                        Entity.json(new Board("", name, password)),
                        Board.class
                );
    }

    /**
     * Gets all boards on this server
     *
     * @param password the password which has been entered by the use
     * @return all the boars on this server
     */
    public List<Board> getAllBoards(String password) {
        return client.target(this.server).path("/api/admin/board/all")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("authorization", "Basic " + Base64.getEncoder().
                        encodeToString(("admin:" + password).getBytes()))
                .get(new GenericType<List<Board>>() {
                });
    }

    /**
     * Creates a new board in the given server with the given password
     *
     * @param key the key of the Board
     */
    public void deleteBoard(final String key) {
        client.target(this.server).path("/api/board/{key}")
                .resolveTemplate("key", key)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .delete().close();
    }

    /**
     * Gets board from the given server by the given key
     *
     * @param key the key
     * @return the relevant board, or null
     */
    public Board getBoard(final String key) {
        try {
            return client.target(this.server).path("/api/board/{key}")
                    .resolveTemplate("key", key)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Board.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Gets board from the given server by the given key
     *
     * @param id    the board id
     * @param board the board data
     * @return the edited board, or null
     */
    public Board editBoard(final int id, final Board board) {
        try {
            return client.target(this.server).path("/api/board/{id}")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .method(HttpMethod.PATCH,
                            Entity.json(new Board(board.getKey(), board.getName(),
                                    board.getPassword(), board.getColour(),
                                    board.getFontColour())),
                            Board.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to create a list
     *
     * @param id    the id of the board to create the list in
     * @param title the title of the list
     * @return the edited board
     */
    public Board createList(final int id, final String title) {
        try {
            return client.target(this.server).path("/api/board/{id}/list")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(
                            Entity.json(new CardList(title)),
                            Board.class
                    );
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to delete a list from the board
     *
     * @param id     the board id
     * @param listId the id of the list to delete from the board
     * @return the edited board
     */
    public Board deleteList(final int id, final int listId) {
        try {
            return client.target(this.server).path("/api/board/{id}/list/{listId}")
                    .resolveTemplate("id", id)
                    .resolveTemplate("listId", listId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(Board.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to edit the list in the board
     *
     * @param id         the list id
     * @param title      the new title
     * @param colour     the new colour
     * @param textColour new text colour
     * @return the renamed list
     */
    public CardList editCardList(final int id, final String title, final String colour,
                                 final String textColour) {
        try {
            return client.target(this.server).path("/api/list/{id}")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .method(HttpMethod.PATCH,
                            Entity.json(new CardList(title, colour, textColour)), CardList.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to create a card
     *
     * @param id          the id of the list to create the card in
     * @param title       the title of the card
     * @param description the title of the card
     * @return the edited board
     */
    public CardList createCard(final int id, final String title, final String description) {
        try {
            return client.target(this.server).path("/api/list/{id}/card")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(
                            Entity.json(new Card(title, description)),
                            CardList.class
                    );
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to delete a card
     *
     * @param id     the id of the list to delete the card from
     * @param cardId the id of the card
     * @return the edited board
     */
    public CardList deleteCard(final int id, final int cardId) {
        try {
            return client.target(this.server).path("/api/list/{id}/card/{cardId}")
                    .resolveTemplate("id", id)
                    .resolveTemplate("cardId", cardId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(CardList.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to move a card
     *
     * @param id       the id of the card
     * @param listId   the list to move the card to
     * @param position the position of the card in the new list
     */
    public void moveCard(final int id, final int listId, final int position) {
        try {
            client.target(this.server).path("/api/card/{id}/move/{listId}/{position}")
                    .resolveTemplate("id", id)
                    .resolveTemplate("listId", listId)
                    .resolveTemplate("position", position)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get().close();
        } catch (NotFoundException e) {
        }
    }

    /**
     * Sends a request to edit the card in a list
     *
     * @param id          the card to edit
     * @param title       the new title
     * @param description the new description
     * @param colour      new colour
     * @return the edited card
     */
    public Card editCard(final int id, final String title, final String description,
                         final String colour) {
        try {
            return client.target(this.server).path("/api/card/{id}")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .method(HttpMethod.PATCH,
                            Entity.json(new Card(title, description, colour)), Card.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Creates a new tag
     *
     * @param boardId id of the board to add the tag to
     * @param name    name of the tag
     * @param colour  colour of the tag
     * @return the board
     */
    public Board createTag(int boardId, String name, int colour) {
        try {
            return client.target(this.server).path("/api/board/{id}/tag")
                    .resolveTemplate("id", boardId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(
                            Entity.json(new Tag(name, colour)),
                            Board.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Deletes a tag
     *
     * @param boardId id of the board where the tag is
     * @param tagId   id of the tag
     * @return returns a board without the tag
     */
    public Board deleteTag(int boardId, int tagId) {
        try {
            return client.target(this.server).path("/api/board/{id}/tag/{tagid}")
                    .resolveTemplate("id", boardId)
                    .resolveTemplate("tagid", tagId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(
                            Board.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Adds a tag to the specified card
     *
     * @param tagId  if of the tag to add
     * @param cardId id of the car where the tag will be added
     * @param tag    tag to add
     * @return the card
     */
    public Card addTag(final int tagId, final int cardId, final Tag tag) {
        try {
            return client.target(this.server).path("/api/card/{cardId}/tag/{tagId}")
                    .resolveTemplate("cardId", cardId)
                    .resolveTemplate("tagId", tagId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .put(Entity.json(tag), Card.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Edits a new tag
     *
     * @param tagId     id of the tag
     * @param newName   name
     * @param newColour colour
     * @return dn
     */
    public Tag editTag(final int tagId, final String newName, final int newColour) {
        try {
            return client.target(this.server).path("/api/tag/{id}")
                    .resolveTemplate("id", tagId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .method(HttpMethod.PATCH,
                            Entity.json(new Tag(newName, newColour)), Tag.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Method to remove an existing tag from a card
     *
     * @param cardId id of the card
     * @param tagId  id of the tag to remove
     * @return the new card without the tag
     */
    public Card removeTagFromCard(final int cardId, final int tagId) {
        try {
            return client.target(this.server).path("/api/card/{cardId}/tag/{tagId}")
                    .resolveTemplate("cardId", cardId)
                    .resolveTemplate("tagId", tagId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(Card.class);
        } catch (NotFoundException e) {
            return null;
        }

    }

    /**
     * Creates a new task and adds it to a card
     *
     * @param cardId    id of the card in which to add the task
     * @param name      name of the task
     * @param completed is the task completed?
     * @return the new card containing the task
     */
    public Card addTaskToCard(final int cardId, final String name, final boolean completed) {
        try {
            return client.target(this.server).path("/api/card/{cardId}/task")
                    .resolveTemplate("cardId", cardId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(
                            Entity.json(new Task(name, completed)),
                            Card.class
                    );
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Edits a task from the server
     *
     * @param id        id of the task
     * @param name      new name of the task
     * @param completed new boolean for completeness of the task
     * @return the edited task
     */
    public Task editTask(final int id, final String name, final boolean completed) {
        try {
            return client.target(this.server).path("/api/task/{id}")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .method(HttpMethod.PATCH,
                            Entity.json(new Task(name, completed)), Task.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Deletes a task
     *
     * @param taskId id of the task to delete
     * @param cardId id of the card from which to remove the task
     * @return the new card
     */
    public Card removeTaskFromCard(int taskId, int cardId) {
        try {
            return client.target(this.server).path("/api/card/{cardId}/task/{taskId}")
                    .resolveTemplate("cardId", cardId)
                    .resolveTemplate("taskId", taskId)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(Card.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Moves a task
     *
     * @param taskId    id of the task to delete
     * @param direction direction of movement
     */
    public void moveTask(int taskId, String direction) {
        try {
            client.target(this.server).path("/api/task/{taskId}/move/{direction}")
                    .resolveTemplate("taskId", taskId)
                    .resolveTemplate("direction", direction)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get();
        } catch (NotFoundException e) {
        }
    }

    /**
     * Registers a listener to receive updates on all boards
     * from the server using the specified password.
     * This method will create a new thread to continuously listen for updates from the server.
     *
     * @param password       the password which has been entered by the user
     * @param boardsListener The listener to be called with a list of updated boards.
     */
    public void registerAllBoardsListener(String password, Consumer<List<Board>> boardsListener) {
        if (this.exec != null) this.exec.shutdownNow();
        this.exec = Executors.newSingleThreadExecutor();
        this.exec.submit(() -> {
            while (!Thread.interrupted()) {
                final Response res = client.target(this.server).path("/api/admin/board/all/updates")
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("authorization", "Basic " + Base64.getEncoder().
                                encodeToString(("admin:" + password).getBytes()))
                        .get(Response.class);
                if (res.getStatus() == 204) continue;

                final List<Board> boards = res.readEntity(new GenericType<>() {
                });
                boardsListener.accept(boards);
                res.close();
            }
        });
    }

    /**
     * Stops current running background threads
     */
    public void stop() {
        if (this.exec != null) this.exec.shutdownNow();
    }

}