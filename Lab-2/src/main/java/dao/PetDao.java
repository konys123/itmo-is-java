package dao;

import entities.Pet;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class PetDao implements GenericDao<Pet> {

    @Override
    public Pet save(Pet pet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(pet);
        tx.commit();
        session.close();
        return pet;
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try (session) {
            Transaction tx = session.beginTransaction();
            Pet pet = session.find(Pet.class, id);
            if (pet == null) throw new EntityNotFoundException("Pet with id " + id + " not found");
            session.remove(pet);
            tx.commit();
        }
    }

    @Override
    public void deleteByEntity(Pet pet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.remove(pet);
        tx.commit();
        session.close();
    }

    @Override
    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<Pet> pets = session.createQuery("FROM Pet", Pet.class).getResultList();

        for (Pet pet : pets) {
            session.remove(pet);
        }

        tx.commit();
        session.close();
    }

    @Override
    public Pet update(Pet pet) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try (session) {
            Transaction tx = session.beginTransaction();
            Pet existingPet = session.find(Pet.class, pet.getId());
            if (existingPet == null) throw new EntityNotFoundException("Pet with id " + pet.getId() + " not found");

            existingPet.setBirthDate(pet.getBirthDate());
            existingPet.setName(pet.getName());
            existingPet.setBreed(pet.getBreed());
            existingPet.setColor(pet.getColor());
            existingPet.setFriends(pet.getFriends());
            existingPet.setOwner(pet.getOwner());

            tx.commit();
            session.close();

            return existingPet;
        }
    }

    @Override
    public Pet getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try (session) {
            Transaction tx = session.beginTransaction();
            Pet pet = session.find(Pet.class, id);
            if (pet == null) throw new EntityNotFoundException("Pet with id " + id + " not found");

            tx.commit();
            session.close();

            return pet;
        }
    }

    @Override
    public List<Pet> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Pet> pets = session.createQuery("FROM Pet", Pet.class).getResultList();
        session.close();
        return pets;
    }
}
