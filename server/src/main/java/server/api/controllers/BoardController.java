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
package server.api.controllers;

import commons.entities.Board;
import commons.entities.CardList;
import commons.entities.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.exceptions.InvalidRequestException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import server.services.BoardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final SimpMessagingTemplate msgs;

    /**
     * Creates the board controller with a service
     *
     * @param boardService the service with the main business logic
     * @param msgs object to send messages to connected websockets
     */
    public BoardController(final BoardService boardService,
                           final SimpMessagingTemplate msgs) {
        this.boardService = boardService;
        this.msgs = msgs;
    }

    /**
     * Handler for creating board
     *
     * @param board the board payload
     * @return the created board
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Board> createBoard(@RequestBody final Board board) {
        return new ResponseEntity<>(this.boardService.createBoard(board.getName(),
                board.getPassword()), new HttpHeaders(), 200);
    }
    /**
     * Handler for getting the board
     *
     * @param key the board key
     * @return the board
     */
    @GetMapping("/{key}")
    public ResponseEntity<Board> getBoard(@PathVariable final String key) {
        return new ResponseEntity<>(
                this.boardService.getBoard(key), new HttpHeaders(), 200);
    }

    /** Handler for creating a tag
     * @param id unique id of the board
     * @param tag the new tag that we are creating
     * @param errors wrapping object for potential validating errors
     * @return the board with the new tag
     */
    @PostMapping("/{id}/tag")
    public ResponseEntity<Board> createTag(@PathVariable final int id,
                                           @Validated @RequestBody final Tag tag,
                                           final BindingResult errors){
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        final Board addedTagToBoard = this.boardService.addTagToBoard(id, tag);
        msgs.convertAndSend("/topic/board", addedTagToBoard);
        return new ResponseEntity<>(addedTagToBoard,
                new HttpHeaders(), 200);
    }

    /**
     * Handler for deleting a tag on a board
     *
     * @param id an id of a board on which this tag exists
     * @param tagId an id of a tag to be deleted
     * @return the board without this tag
     */
    @DeleteMapping("/{id}/tag/{tagId}")
    public ResponseEntity<Board> deleteTag(@PathVariable final int id,
                                           @PathVariable final int tagId) {
        final Board deletedTagFromBoard = this.boardService.deleteTagFromBoard(id, tagId);
        msgs.convertAndSend("/topic/board");
        return new ResponseEntity<>(deletedTagFromBoard,
                new HttpHeaders(), 200);
    }

    /**
     * Handler for creating the list in a board
     *
     * @param id the board to create a list for
     * @param cardList the data for the new list
     * @param errors wrapping object for potential validating errors
     * @return the board with its new list
     */
    @PostMapping("/{id}/list")
    public ResponseEntity<Board> createList(@PathVariable final int id,
                                            @Validated @RequestBody final CardList cardList,
                                            final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        final Board createdListToBoard = this.boardService.createListInBoard(id, cardList);
        msgs.convertAndSend("/topic/board", createdListToBoard);
        return new ResponseEntity<>(createdListToBoard,
                new HttpHeaders(), 200);
    }

    /**
     * Handler for deleting the list of a board
     *
     * @param id     the board id
     * @param listId the list id to delete
     * @return the board without the list
     */
    @DeleteMapping("/{id}/list/{listId}")
    public ResponseEntity<Board> deleteList(@PathVariable final int id,
                                            @PathVariable final int listId) {
        final Board deleteListFromBoard = this.boardService.deleteListFromBoard(id, listId);
        msgs.convertAndSend("/topic/board", deleteListFromBoard);
        return new ResponseEntity<>(deleteListFromBoard,
                new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * endpoint for editing the title of a card list
     *
     * @param id     int value representing the id of a Board
     * @param board  the Board being edited
     * @param errors wrapping object for potential validating errors
     * @return the card list with the changed new title
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Board> editPassword(@PathVariable final int id,
                                              @RequestBody final Board board,
                                              final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        final Board editedPasswordBoard = this.boardService.changePassword(id, board);
        msgs.convertAndSend("/topic/board", editedPasswordBoard);
        return new ResponseEntity<>(editedPasswordBoard,
                new HttpHeaders(), 200);
    }

}