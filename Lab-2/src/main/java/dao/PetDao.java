package dao;

import entities.Pet;
import exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.util.List;

public class PetDao implements GenericDao<Pet> {
    private final SessionFactory sessionFactory;

    public PetDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Pet save(Pet pet) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(pet);

        tx.commit();
        session.close();

        return pet;
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();

            Pet pet = session.find(Pet.class, id);
            if (pet == null) throw new EntityNotFoundException("Pet with id " + id + " not found");

            session.remove(pet);
            tx.commit();
        }
    }

    @Override
    public void deleteByEntity(Pet pet) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();

            Pet existingPet = session.find(Pet.class, pet.getId());
            if (existingPet == null) throw new EntityNotFoundException("Pet with id " + pet.getId() + " not found");

            session.remove(existingPet);

            tx.commit();
        }
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List<Pet> pets = session.createQuery("FROM Pet", Pet.class).getResultList();

        for (Pet pet : pets) {
            session.remove(pet);
        }

        tx.commit();
        session.close();
    }

    @Override
    public Pet update(Pet pet) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

        try (session) {
            Transaction tx = session.beginTransaction();
            Pet existingPet = session.find(Pet.class, pet.getId());
            if (existingPet == null)
                throw new EntityNotFoundException("Pet with id " + pet.getId() + " not found");

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
    public Pet getById(Long id) throws EntityNotFoundException {
        Session session = sessionFactory.openSession();

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
        Session session = sessionFactory.openSession();
        List<Pet> pets = session.createQuery("FROM Pet", Pet.class).getResultList();
        session.close();
        return pets;
    }
}
