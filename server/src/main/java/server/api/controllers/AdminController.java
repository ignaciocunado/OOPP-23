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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.BoardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public final class AdminController {

    private final BoardService boardService;

    /**
     * Creates the board controller with a service
     *
     * @param boardService the service with the main business logic
     */
    public AdminController(final BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Handler for getting all boards
     *
     * @return the list boards
     */
    @GetMapping("/board/all")
    public ResponseEntity<List<Board>> getAllBoards() {
        return new ResponseEntity<>(this.boardService.getAllBoards(), new HttpHeaders(), 200);
    }

    /**
     * Handler for getting all boards
     *
     * @return the list boards
     */
    @GetMapping("/board/all/updates")
    public DeferredResult<ResponseEntity<List<Board>>> getAllBoardsUpdates() {
        final ResponseEntity<List<Board>> noContent = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        final DeferredResult<ResponseEntity<List<Board>>> res = new DeferredResult<>(10000L, noContent);
        final UUID runId = this.boardService.addAllBoardListener(() -> {
            res.setResult(new ResponseEntity<>(this.boardService.getAllBoards(), new HttpHeaders(), 200));
        });
        res.onCompletion(() -> {
            this.boardService.removeAllBoardListener(runId);
        });;
        return res;
    }

}