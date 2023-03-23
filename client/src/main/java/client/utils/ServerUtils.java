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

import java.util.List;

import commons.entities.Board;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;

import commons.entities.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private Client client;
    private String server;
    public ServerUtils(final String server) {
        this.client = ClientBuilder.newClient(new ClientConfig());
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
     * Gets board from the given server by the given id
     * @return the relevant board, or null
     */
    public Board getBoard(final String key) {
        return client.target(this.server).path("api/board/{key}")
                .resolveTemplate("key", key)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(Board.class);
    }
}