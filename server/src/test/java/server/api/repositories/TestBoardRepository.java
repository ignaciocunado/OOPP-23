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
package server.api.repositories;

import commons.entities.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import server.database.BoardRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class TestBoardRepository implements BoardRepository {

    private int nextInt = 0;
    public final List<Board> boards = new ArrayList<>();

    @Override
    public List<Board> findAll() {
        return this.boards;
    }

    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Board> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return this.boards.size();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Board entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Board> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Board> S save(S entity) {
        for (final Board board : boards) {
            if (board.getId() == entity.getId()) {
                board.setPassword(entity.getPassword());
                return (S) board;
            }
        }

        nextInt++;
        final Board board = new Board(entity.getKey(), entity.getPassword());
        board.setId(nextInt);
        entity.setId(nextInt);

        this.boards.add(board);
        return (S) board;
    }

    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Board> findById(Integer integer) {
        return this.find(integer);
    }

    @Override
    public boolean existsById(Integer integer) {
        return this.find(integer).isPresent();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Board getOne(Integer integer) {
        return null;
    }

    @Override
    public Board getById(Integer integer) {
        final Optional<Board> boardOpt = find(integer);
        if (!boardOpt.isPresent()) throw new JpaObjectRetrievalFailureException(new EntityNotFoundException());
        return boardOpt.get();
    }

    private Optional<Board> find(final int id) {
        return this.boards.stream().filter(b -> b.getId() == id).findFirst();
    }

    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Board getBoardByKey(String key) {
        return this.findBoardByKey(key).get();
    }

    @Override
    public Optional<Board> findBoardByKey(String key) {
        return this.boards.stream().filter(b -> b.getKey().equals(key)).findFirst();
    }


}