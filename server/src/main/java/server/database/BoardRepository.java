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
package server.database;

import commons.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    /**
     * Gets the board from the repository by a key
     * @param key the key
     * @return the board
     */
    @Query("SELECT b FROM Board b WHERE b.key = ?1")
    Board getBoardByKey(final String key);

    /**
     * Finds the board from the repository by a key
     * @param key the key
     * @return the board
     */
    @Query("SELECT b FROM Board b WHERE b.key = ?1")
    Optional<Board> findBoardByKey(final String key);

}