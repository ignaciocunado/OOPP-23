package server.database;

import commons.Board;
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

public class TaskRepositoryTest implements TaskRepository{

    private final List<Task> tasks = new ArrayList<>();
    private int nextInt = 0;

    @Override
    public List<Task> findAll() {
        return this.tasks;
    }

    @Override
    public List<Task> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Task> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return this.tasks.size();
    }

    @Override
    public void deleteById(Integer integer) {
        for(Task t : tasks) {
            if(t.getId() == integer) {
                tasks.remove(t);
                return;
            }
        }
    }

    @Override
    public void delete(Task entity) {
        tasks.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Task> S save(S entity) {

    }

    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Task> findById(Integer integer) {
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
    public <S extends Task> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Task getOne(Integer integer) {
        return null;
    }

    @Override
    public Task getById(Integer integer) {
        final Optional<Task> taskOpt = find(integer);
        if (!taskOpt.isPresent()) throw new JpaObjectRetrievalFailureException(new EntityNotFoundException());
        return taskOpt.get();

    }

    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Task> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Task> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Task, R> R findBy(Example<S> example,
                                        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    private Optional<Task> find(int id) {
        return this.tasks.stream().filter(b -> b.getId() == id).findFirst();
    }
}