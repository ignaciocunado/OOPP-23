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
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import org.springframework.http.HttpStatus;
import server.database.CardRepository;
import server.database.TagRepository;
import server.exceptions.EntityNotFoundException;
import server.exceptions.InvalidRequestException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import server.services.TextService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardRepository boardRepo;
    private final TagRepository tagRepo;
    private final CardListRepository cardListRepository;
    private final CardRepository cardRepository;
    private final TextService textService;

    /**
     * RestAPI Controller for the board route
     *
     * @param boardRepo          repository for boards
     * @param cardListRepository repository for cardLists
     * @param cardRepository     repository for cards
     * @param textService        service for generating random keys
     * @param tagRepo repository for tags
     */
    public BoardController(final BoardRepository boardRepo,
                           final CardListRepository cardListRepository,
                           final CardRepository cardRepository,
                           final TextService textService,
                           final TagRepository tagRepo) {
        this.boardRepo = boardRepo;
        this.cardListRepository = cardListRepository;
        this.cardRepository = cardRepository;
        this.textService = textService;
        this.tagRepo = tagRepo;
    }

    /**
     * Handler for creating board
     *
     * @param request the board payload
     * @return the created board
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Board> createBoard(@RequestBody final Board request) {
        final String newKey = this.textService.randomAlphanumericalString(10);
        final Board board = new Board(newKey, request.getPassword());
        return new ResponseEntity<>(this.boardRepo.save(board), new HttpHeaders(), 200);
    }
    /**
     * Handler for getting the board
     *
     * @param id the board id
     * @return the board
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable final Integer id) {
        if (!this.boardRepo.existsById(id)) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        return new ResponseEntity<>(this.boardRepo.getById(id), new HttpHeaders(), 200);
    }

    /** Hnadler for creating a tag
     * @param id unique id of the board
     * @param tag the new tag that we are creating
     * @param errors wrapping for potential validating errors
     * @return the board with the new tag
     */
    @PostMapping("/{id}/tag")
    public ResponseEntity<Board> createTag(@PathVariable final int id,
                                           @Validated @RequestBody Tag tag,
                                           final BindingResult errors){
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if (!boardRepo.existsById(id)) {
            throw new EntityNotFoundException("No board with id " + id);
        }
        tagRepo.save(tag);
        Board board = boardRepo.getById(id);
        board.addTag(tag);
        return new ResponseEntity<>(boardRepo.save(board), new HttpHeaders(), 200);
    }
    /**
     * Handler for creating the list in a board
     *
     * @param id the board to create a list for
     * @param payload the data for the new list
     * @param errors wrapping object for potential validating errors
     * @return the board with its new list
     */
    @PostMapping("/{id}/list")
    public ResponseEntity<Board> createList(@PathVariable final Integer id,
                                            @Validated @RequestBody final CardList payload,
                                            final BindingResult errors) {
        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }

        if (!this.boardRepo.existsById(id)) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final CardList cardList = new CardList(payload.getTitle());
        this.cardListRepository.save(cardList);

        final Board board = this.boardRepo.getById(id);
        board.addList(cardList);
        return new ResponseEntity<>(this.boardRepo.save(board), new HttpHeaders(), 200);
    }

    /**
     * Handler for deleting the list of a board
     *
     * @param id     the board id
     * @param listId the list id to delete
     * @return the board without the list
     */
    @DeleteMapping("/{id}/list/{listId}")
    public ResponseEntity<Board> deleteList (@PathVariable final Integer id,
                                             @PathVariable final Integer listId) {
        if (!this.boardRepo.existsById(id)) {
            throw new EntityNotFoundException("No board with id " + id);
        }

        final Board board = this.boardRepo.getById(id);
        if (!board.removeListById(listId)) {
            throw new EntityNotFoundException("Board contains no list with id " + listId);
        }

        final List<Card> cards =
                new ArrayList<>(this.cardListRepository.getById(listId).getCards());
        this.cardListRepository.deleteById(listId);
        cards.forEach(card -> this.cardRepository.deleteById(card.getId()));

        return new ResponseEntity<>(this.boardRepo.save(board), new HttpHeaders(), HttpStatus.OK);
    }

    /** endpoint for editing the title of a card list
     *
     * @param id int value representing the id of a Board
     * @param board the Board being edited
     * @param errors wrapping object for potential validating errors
     * @return the card list with the changed new title
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Board> editPassword(@PathVariable final Integer id,
                                              @Validated @RequestBody
                                              final Board board,
                                              final BindingResult errors) {
        if(errors.hasErrors()) {
            throw new InvalidRequestException(errors);
        }
        if (!this.boardRepo.existsById(id)) {
            throw new EntityNotFoundException("No board with id " + id);
        }
        Board editedBoard = boardRepo.getById(id);
        editedBoard.setPassword(board.getPassword());
        return new ResponseEntity<>(this.boardRepo.save(editedBoard), new HttpHeaders(), 200);
    }

}