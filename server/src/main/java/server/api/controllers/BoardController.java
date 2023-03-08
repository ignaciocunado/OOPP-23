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
import commons.Quote;
import commons.TextUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.QuoteRepository;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardRepository boardRepo;
    private final CardListRepository cardListRepository;

    /**
     * RestAPI Controller for the board route
     * @param boardRepo
     */
    public BoardController(final BoardRepository boardRepo, final CardListRepository cardListRepository) {
        this.boardRepo = boardRepo;
        this.cardListRepository = cardListRepository;
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> createBoard(@RequestBody final Board request) {
        final String newKey = TextUtils.randomAlphanumericalString(10);
        final Board board = new Board(newKey, request.getPassword());
        return new ResponseEntity<>(this.boardRepo.save(board), new HttpHeaders(), 200);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable final Integer id) {
        return new ResponseEntity<>(this.boardRepo.getById(id), new HttpHeaders(), 200);
    }

    @PostMapping("/{id}/list")
    public ResponseEntity<Board> createList(@PathVariable final Integer id) {
        final CardList list = new CardList("New List");
        this.cardListRepository.save(list);

        final Board board = this.boardRepo.getById(id);
        board.getListsOnBoard().add(list);
        this.boardRepo.save(board);
        return new ResponseEntity<>(this.boardRepo.getById(id), new HttpHeaders(), 200);
    }

}