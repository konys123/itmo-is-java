package repositories;

import entities.Pet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;

import java.util.List;

public class PostgresPetRepository {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("konys123-persistence-unit");

    public Pet save(Pet pet) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(pet);
        em.getTransaction().commit();
        em.close();
        return pet;
    }

    public void deleteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pet pet = em.find(Pet.class, id);
            if (pet == null) throw new EntityNotFoundException("Pet with id " + id + " not found");
            em.remove(pet);
            em.getTransaction().commit();
            em.close();

        } finally {
            em.close();
        }
    }

    public void deleteByEntity(Pet pet) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(pet);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteAll() {
        EntityManager em = emf.createEntityManager();
        List<Pet> pets = em.createQuery("FROM Pet", Pet.class).getResultList();
        em.getTransaction().begin();

        for (Pet pet : pets) {
            em.remove(pet);
        }

        em.getTransaction().commit();
        em.close();
    }

    public Pet update(Pet pet) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Pet existingPet = em.find(Pet.class, pet.getId());
            if (existingPet == null) throw new EntityNotFoundException("Pet with id " + pet.getId() + " not found");

            existingPet.setBirthDate(pet.getBirthDate());
            existingPet.setName(pet.getName());
            existingPet.setBreed(pet.getBreed());
            existingPet.setColor(pet.getColor());
            existingPet.setFriends(pet.getFriends());
            existingPet.setOwner(pet.getOwner());

            em.getTransaction().commit();
            em.close();

            return existingPet;
        } finally {
            em.close();
        }
    }

    public Pet getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Pet pet = em.find(Pet.class, id);
            if (pet == null) throw new EntityNotFoundException("Pet with id " + id + " not found");

            em.getTransaction().commit();
            em.close();

            return pet;
        } finally {
            em.close();
        }
    }

    public List<Pet> getAll() {
        EntityManager em = emf.createEntityManager();
        List<Pet> pets = em.createQuery("FROM Pet", Pet.class).getResultList();
        em.close();
        return pets;
    }
}
