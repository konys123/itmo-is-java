package dao;

import exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDao<T> implements IDao<T> {
    protected final SessionFactory sessionFactory;
    protected final Class<T> entityClass;

    protected AbstractDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.entityClass = (Class<T>) ((ParameterizedType)
                getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T save(T entity) {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();

            session.persist(entity);

            tx.commit();
            session.close();

            return entity;
        }
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();

            T entity = session.find(entityClass, id);
            if (entity == null)
                throw new EntityNotFoundException(entityClass.getSimpleName() + " with id " + id + " not found");

            session.remove(entity);
            tx.commit();
        }
    }

    @Override
    public void deleteByEntity(T entity) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
        }
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List<T> entities = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();

        for (T entity : entities) {
            session.remove(entity);
        }

        tx.commit();
        session.close();
    }

    @Override
    public T update(T entity) {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            session.close();

            return entity;
        }
    }

    @Override
    public T getById(Long id) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();

            T entity = session.find(entityClass, id);
            if (entity == null)
                throw new EntityNotFoundException(entityClass.getSimpleName() + " with id " + id + " not found");

            tx.commit();
            session.close();

            return entity;
        }
    }

    @Override
    public List<T> getAll() {
        Session session = sessionFactory.openSession();
        List<T> entities = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
        session.close();
        return entities;
    }
}
