package services;

import entities.Pet;
import repositories.PostgresPetRepository;

public class PetService {
    private final PostgresPetRepository postgresPetRepository = new PostgresPetRepository();

    public void createPet(String name, String breed) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setBreed(breed);
        postgresPetRepository.save(pet);
    }
}
