package dao;

import entities.Owner;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class OwnerDao implements GenericDao<Owner> {

    @Override
    public Owner save(Owner owner) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(owner);
        tx.commit();
        session.close();
        return owner;
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            Owner owner = session.find(Owner.class, id);
            if (owner == null) throw new EntityNotFoundException("owner with id " + id + " not found");
            session.remove(owner);
            tx.commit();
        }
    }

    @Override
    public void deleteByEntity(Owner owner) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.remove(owner);
        tx.commit();
        session.close();
    }

    @Override
    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<Owner> owners = session.createQuery("FROM Owner", Owner.class).getResultList();

        for (Owner owner : owners) {
            session.remove(owner);
        }

        tx.commit();
        session.close();
    }

    @Override
    public Owner update(Owner owner) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            Owner existingowner = session.find(Owner.class, owner.getId());
            if (existingowner == null)
                throw new EntityNotFoundException("owner with id " + owner.getId() + " not found");

            existingowner.setBirthDate(owner.getBirthDate());
            existingowner.setName(owner.getName());
            existingowner.setPets(owner.getPets());

            tx.commit();
            session.close();

            return existingowner;
        }
    }

    @Override
    public Owner getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Owner> owners = session.createQuery("FROM Owner", Owner.class).getResultList();
        session.close();
        return owners;
    }
}
