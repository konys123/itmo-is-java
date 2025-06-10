package httpWebGateway.dao;

import exceptions.EntityNotFoundException;

import java.util.List;

public interface Dao<T> {
    T save(T entity);

    void deleteById(Long id) throws EntityNotFoundException;

    void deleteByEntity(T entity) throws EntityNotFoundException;

    void deleteAll();

    T update(T entity) throws EntityNotFoundException;

    T getById(Long id) throws EntityNotFoundException;

    List<T> getAll();
}
