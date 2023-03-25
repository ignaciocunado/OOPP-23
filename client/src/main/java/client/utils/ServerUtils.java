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
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import java.io.IOException;

@Singleton
public class ServerUtils {

    private Client client;
    private String server;

    /**
     * Empty constructor
     */
    public ServerUtils() {
        this.client = ClientBuilder.newClient(new ClientConfig());
    }

    /**
     * Object that has utility methods for accessing the server
     * @param server the server string
     */
    public ServerUtils(final String server) {
        this.client = ClientBuilder.newClient(new ClientConfig());
        this.server = server;
    }

    /**
     * Gets current server string
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets current server string
     * @param server the new server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Gets board from the given server by the given id
     * @return the relevant board, or null
     */
    /**
     * Creates a new board in the given server with the given password
     * @param password the password to create the board with
     * @return the newly created board
     */
    public Board createBoard(final String password) {
        return client.target(this.server).path("api/board")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(
                        Entity.entity(new Board("", password), MediaType.APPLICATION_JSON),
                        Board.class
                );
    }

    /**
     * Gets board from the given server by the given key
     * @param key the key
     * @return the relevant board, or null
     */
    public Board getBoard(final String key) {
        try {
            return client.target(this.server).path("api/board/{key}")
                    .resolveTemplate("key", key)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Board.class);
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to create a list
     * @param id the id of the board to create the list in
     * @param title the title of the list
     * @return the edited board
     */
    public Board createList(final int id, final String title) {
        try {
            return client.target(this.server).path("api/board/{id}/list")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(
                            Entity.entity(new CardList(title), MediaType.APPLICATION_JSON),
                            Board.class
                    );
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to delete a list from the board
     * @param id the board id
     * @param listId the id of the list to delete from the board
     * @return the edited board
     */
    public Board deleteList(final int id, final int listId) {
        try {
            return client.target(this.server).path("api/board/{id}/list/{listId}")
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
     * TODO: Acutally properly implement with JAX RS WS client
     * @param id
     * @param title
     */
    public void renameList(final int id, final String title) {
        try {
            final HttpClient client = HttpClients.createDefault();
            final HttpPatch request = new HttpPatch(this.server+"api/list/"+id);
            final StringEntity entity = new StringEntity(String.format("{\"title\": \"%s\"}", title));
            request.setEntity(entity);
            request.setHeader("content-type", "application/json");
            client.execute(request, response -> null);
        } catch (NotFoundException | IOException e) {
            return;
        }
    }

    /**
     * Sends a request to create a card
     * @param id the id of the list to create the card in
     * @param title the title of the card
     * @param description the title of the card
     * @return the edited board
     */
    public CardList createCard(final int id, final String title, final String description) {
        try {
            return client.target(this.server).path("api/list/{id}/card")
                    .resolveTemplate("id", id)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(
                            Entity.entity(new Card(title, description), MediaType.APPLICATION_JSON),
                            CardList.class
                    );
        } catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * Sends a request to delete a card
     * @param id the id of the list to delete the card from
     * @param cardId the id of the card
     * @return the edited board
     */
    public CardList deleteCard(final int id, final int cardId) {
        try {
            return client.target(this.server).path("api/list/{id}/card/{cardId}")
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
     * @param id the id of the card
     * @param listId the list to move the card to
     * @param position the position of the card in the new list
     * @return the edited board
     */
    public void moveCard(final int id, final int listId, final int position) {
        try {
            client.target(this.server).path("api/card/{id}/move/{listId}/{position}")
                    .resolveTemplate("id", id)
                    .resolveTemplate("listId", listId)
                    .resolveTemplate("position", position)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get().close();
        } catch (NotFoundException e) {}
    }

    /**
     * TODO: Acutally properly implement with JAX RS WS client
     * @param id
     * @param title
     * @param description
     */
    public void editCard(final int id, final String title, final String description) {
        try {
            final HttpClient client = HttpClients.createDefault();
            final HttpPatch request = new HttpPatch(this.server+"api/card/"+id);
            final StringEntity entity = new StringEntity(String.format("{\"title\": \"%s\", \"description\": \"%s\"}", title, description));
            request.setEntity(entity);
            request.setHeader("content-type", "application/json");
            client.execute(request, response -> null);
        } catch (NotFoundException | IOException e) {
            return;
        }
    }
}