package server.api.repositories;

import commons.entities.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import server.database.CardRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestCardRepository implements CardRepository {

    private final List<Card> cards = new ArrayList<>();
    private int nextInt = 0;

    @Override
    public List<Card> findAll() {
        return this.cards;
    }

    @Override
    public List<Card> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Card> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return this.cards.size();
    }

    @Override
    public void deleteById(Integer integer) {
        cards.removeIf(card -> card.getId() == integer);
    }

    @Override
    public void delete(Card entity) {
        this.cards.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Card> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Card> S save(S entity) {
        for(Card c : cards) {
            if(c.getId() == entity.getId()) {
                c.setTitle(entity.getTitle());
                c.setDescription(entity.getDescription());
                c.setNestedTaskList(entity.getNestedTaskList());
                c.setTags(entity.getTags());
                return (S) c;
            }
        }

        final Card card = new Card(entity.getTitle(), entity.getDescription());
        nextInt++;
        card.setId(nextInt);
        entity.setId(nextInt);
        this.cards.add(card);
        return (S) card;
    }

    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Card> findById(Integer integer) {
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
    public <S extends Card> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Card getOne(Integer integer) {
        return null;
    }

    @Override
    public Card getById(Integer integer) {
        final Optional<Card> cardopt = find(integer);
        if (!cardopt.isPresent()) throw new JpaObjectRetrievalFailureException(new EntityNotFoundException());
        return cardopt.get();

    }

    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Card, R> R findBy(Example<S> example,
                                        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    private Optional<Card> find(int id) {
        return this.cards.stream().filter(b -> b.getId() == id).findFirst();
    }

    @Override
    public List<Integer> selectCardsWithTag(int tagId) {
        //for the sake of the test this query is omitted
        return List.of(1);
    }
}