package repositories;

import entities.Owner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;

import java.util.List;

public class PostgresOwnerRepository {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("konys123-persistence-unit");

    public Owner save(Owner owner) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(owner);
        em.getTransaction().commit();
        em.close();
        return owner;
    }

    public void deleteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Owner owner = em.find(Owner.class, id);
            if (owner == null) throw new EntityNotFoundException("owner with id " + id + " not found");
            em.remove(owner);
            em.getTransaction().commit();
            em.close();

        } finally {
            em.close();
        }
    }

    public void deleteByEntity(Owner owner) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(owner);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteAll() {
        EntityManager em = emf.createEntityManager();
        List<Owner> owners = em.createQuery("FROM owner", Owner.class).getResultList();
        em.getTransaction().begin();

        for (Owner owner : owners) {
            em.remove(owner);
        }

        em.getTransaction().commit();
        em.close();
    }

    public Owner update(Owner owner) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Owner existingowner = em.find(Owner.class, owner.getId());
            if (existingowner == null)
                throw new EntityNotFoundException("owner with id " + owner.getId() + " not found");

            existingowner.setBirthDate(owner.getBirthDate());
            existingowner.setName(owner.getName());
            existingowner.setPets(owner.getPets());

            em.getTransaction().commit();
            em.close();

            return existingowner;
        } finally {
            em.close();
        }
    }

    public Owner getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Owner owner = em.find(Owner.class, id);
            if (owner == null) throw new EntityNotFoundException("owner with id " + id + " not found");

            em.getTransaction().commit();
            em.close();

            return owner;
        } finally {
            em.close();
        }
    }

    public List<Owner> getAll() {
        EntityManager em = emf.createEntityManager();
        List<Owner> owners = em.createQuery("FROM owner", Owner.class).getResultList();
        em.close();
        return owners;
    }
}
