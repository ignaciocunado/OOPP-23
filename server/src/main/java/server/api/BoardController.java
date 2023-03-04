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
package server.api;

import commons.Board;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardRepository repo;

    /**
     * to do
     * @param random
     * @param repo
     */
    public BoardController(Random random, BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * to do
     * @return to do
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    /**
     * to do
     * @return to do
     */
    @PostMapping(path = {"", "/"})
    public String addBoard() {
        repo.save(new Board("abc", ""));
        return "abc";
    }
}