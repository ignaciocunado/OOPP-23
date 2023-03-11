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

import commons.CardList;
import commons.Quote;
import commons.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import server.database.CardListRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class TestCardListRepository implements CardListRepository {

    private final List<CardList> cardList = new ArrayList<>();
    private int nextInt = 0;

    @Override
    public List<CardList> findAll() {
        return this.cardList;
    }

    @Override
    public List<CardList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CardList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<CardList> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return this.cardList.size();
    }

    @Override
    public void deleteById(Integer integer) {
        cardList.removeIf(cardList -> (cardList.getId() == integer));
    }

    @Override
    public void delete(CardList entity) {
        this.cardList.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends CardList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CardList> S save(S entity) {
        for(CardList cl : cardList){
            if(cl.getId() == entity.getId()) {
                cl.setId(entity.getId());
                cl.setTitle(entity.getTitle());
                return (S) cl;
            }
        }

        final CardList cardList = new CardList(entity.getTitle());
        nextInt++;
        cardList.setId(nextInt);
        this.cardList.add(cardList);
        return (S) cardList;
    }

    @Override
    public <S extends CardList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CardList> findById(Integer integer) {
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
    public <S extends CardList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends CardList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<CardList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CardList getOne(Integer integer) {
        return null;
    }

    @Override
    public CardList getById(Integer integer) {
        final Optional<CardList> cardListOpt = find(integer);
        if (!cardListOpt.isPresent()) throw new JpaObjectRetrievalFailureException(new EntityNotFoundException());
        return cardListOpt.get();
    }

    @Override
    public <S extends CardList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CardList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CardList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CardList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CardList, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    private Optional<CardList> find(int id) {
        return this.cardList.stream().filter(b -> b.getId() == id).findFirst();
    }
}