package server.database;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class TagRepositoryTest implements TagRepository{

    private final List<Tag> tags = new ArrayList<>();
    private int nextInt = 0;

    @Override
    public List<Tag> findAll() {
        return this.tags;
    }

    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tag> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return this.tags.size();
    }

    @Override
    public void deleteById(Integer integer) {
        for(Tag t : tags) {
            if(t.getId() == integer) {
                tags.remove(t);
                return;
            }
        }
    }

    @Override
    public void delete(Tag entity) {
        tags.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Tag> S save(S entity) {
        final Tag tag;
        if(this.findById(entity.getId()).isPresent()) {
            tag = this.getById(entity.getId());
            tag.setName(entity.getName());
            tag.setColour(entity.getColour());
        }
        else {
            tag = new Tag(entity.getName(), entity.getColour());
            tag.setId(nextInt++);
            this.tags.add(tag);
        }
        return (S) tag;
    }

    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Tag> findById(Integer integer) {
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
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Tag getOne(Integer integer) {
        return null;
    }

    @Override
    public Tag getById(Integer integer) {
        final Optional<Tag> tagOpt = find(integer);
        if (!tagOpt.isPresent()) throw new JpaObjectRetrievalFailureException(new EntityNotFoundException());
        return tagOpt.get();

    }

    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Tag, R> R findBy(Example<S> example,
                                       Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    private Optional<Tag> find(int id) {
        return this.tags.stream().filter(b -> b.getId() == id).findFirst();
    }
}