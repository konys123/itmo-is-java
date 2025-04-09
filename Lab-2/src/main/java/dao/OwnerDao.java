package dao;

import entities.Owner;
import exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class OwnerDao implements GenericDao<Owner> {
    private final SessionFactory sessionFactory;

    public OwnerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Owner save(Owner owner) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(owner);

        tx.commit();
        session.close();

        return owner;
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();

            Owner owner = session.find(Owner.class, id);
            if (owner == null) throw new EntityNotFoundException("owner with id " + id + " not found");

            session.remove(owner);

            tx.commit();
        }
    }

    @Override
    public void deleteByEntity(Owner owner) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();

            Owner existingOwner = session.find(Owner.class, owner.getId());
            if (existingOwner == null)
                throw new EntityNotFoundException("owner with id " + owner.getId() + " not found");

            session.remove(existingOwner);

            tx.commit();
        }
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List<Owner> owners = session.createQuery("FROM Owner", Owner.class).getResultList();

        for (Owner owner : owners) {
            session.remove(owner);
        }

        tx.commit();
        session.close();
    }

    @Override
    public Owner update(Owner owner) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();

            Owner existingOwner = session.find(Owner.class, owner.getId());
            if (existingOwner == null)
                throw new EntityNotFoundException("owner with id " + owner.getId() + " not found");

            existingOwner.setBirthDate(owner.getBirthDate());
            existingOwner.setName(owner.getName());
            existingOwner.setPets(owner.getPets());

            tx.commit();
            session.close();

            return existingOwner;
        }
    }

    @Override
    public Owner getById(Long id) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();
            Owner owner = session.find(Owner.class, id);
            if (owner == null) throw new EntityNotFoundException("owner with id " + id + " not found");

            tx.commit();
            session.close();

            return owner;
        }
    }

    @Override
    public List<Owner> getAll() {
        Session session = sessionFactory.openSession();
        List<Owner> owners = session.createQuery("FROM Owner", Owner.class).getResultList();
        session.close();
        return owners;
    }
}
