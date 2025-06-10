package services;

import dao.PetDao;
import entities.Pet;
import exceptions.EntityNotFoundException;

import java.util.List;

public class PetService {
    private final PetDao petDao;

    public PetService(PetDao petDao) {
        this.petDao = petDao;
    }

    public Pet savePet(Pet pet) {
        return petDao.save(pet);
    }

    public void deletePetById(Long id) throws EntityNotFoundException {
        petDao.deleteById(id);
    }

    public void deletePet(Pet pet) throws EntityNotFoundException {
        petDao.deleteByEntity(pet);
    }

    public void deleteAllPets() {
        petDao.deleteAll();
    }

    public Pet updatePet(Pet pet) throws EntityNotFoundException {
        return petDao.update(pet);
    }

    public Pet getPetById(Long id) throws EntityNotFoundException {
        return petDao.getById(id);
    }

    public List<Pet> getAllPets() {
        return petDao.getAll();
    }
}
