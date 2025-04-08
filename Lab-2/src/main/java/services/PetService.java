package services;

import dao.PetDao;
import entities.Pet;

import java.util.List;

public class PetService {
    private final PetDao petDao = new PetDao();

    public Pet savaPet(Pet pet) {
        return petDao.save(pet);
    }

    public void deletePetById(Long id) {
        petDao.deleteById(id);
    }

    public void deletePet(Pet pet) {
        petDao.deleteByEntity(pet);
    }

    public void deleteAllPets() {
        petDao.deleteAll();
    }

    public Pet updatePet(Pet pet) {
        return petDao.update(pet);
    }

    public Pet getPetById(Long id) {
        return petDao.getById(id);
    }

    public List<Pet> getAllPets() {
        return petDao.getAll();
    }
}
