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

import commons.Board;
import commons.CardList;
import org.springframework.http.HttpStatus;
import server.services.TextService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardRepository boardRepo;
    private final CardListRepository cardListRepository;
    private final TextService textService;

    /**
     * RestAPI Controller for the board route
     *
     * @param boardRepo          repository for boards
     * @param cardListRepository repository for cards
     * @param textService        service for generating random keys
     */
    public BoardController(final BoardRepository boardRepo,
                           final CardListRepository cardListRepository,
                           final TextService textService) {
        this.boardRepo = boardRepo;
        this.cardListRepository = cardListRepository;
        this.textService = textService;
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
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(this.boardRepo.getById(id), new HttpHeaders(), 200);
    }

    /**
     * Handler for creating the list in a board
     *
     * @param id the board to create a list for
     * @return the board with its new list
     */
    @PostMapping("/{id}/list")
    public ResponseEntity<Board> createList(@PathVariable final Integer id, @RequestBody final CardList cardList) {
        if (!this.boardRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<Board> deleteList(@PathVariable final Integer id,
                                            @PathVariable final Integer listId) {
        if (!this.boardRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        final Board board = this.boardRepo.getById(id);
        if (!board.removeListById(listId)) {
            return ResponseEntity.notFound().build();
        }

        this.cardListRepository.deleteById(listId);
        return new ResponseEntity<>(this.boardRepo.save(board), new HttpHeaders(), HttpStatus.OK);
    }

}