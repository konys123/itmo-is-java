package dao;

import java.util.List;

public interface GenericDao<T> {
    T save(T entity);

    void deleteById(Long id);

    void deleteByEntity(T entity);

    void deleteAll();

    T update(T entity);

    T getById(Long id);

    List<T> getAll();
}
