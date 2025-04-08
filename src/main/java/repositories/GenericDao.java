package repositories;

import java.util.List;

public interface GenericDao<T> {
    public T save(T entity);

    public void deleteById(Long id);

    public void deleteByEntity(T entity);

    public void deleteAll();

    public T update(T entity);

    public T getById(Long id);

    public List<T> getAll();
}
