import dao.PetDao;
import entities.Colors;
import entities.Pet;
import exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.PetService;

import java.util.List;


public class PetTests extends AbstractTest {

    @Test
    public void savePet() throws EntityNotFoundException {
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);

        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        newPet = petService.savePet(newPet);

        Assertions.assertEquals("cat", petService.getPetById(newPet.getId()).getName());

        petService.deleteAllPets();
    }

    @Test
    public void deletePetById() throws EntityNotFoundException {
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);

        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        newPet = petService.savePet(newPet);
        petService.deletePetById(newPet.getId());

        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(1L));
    }

    @Test
    public void deletePetByEntity() throws EntityNotFoundException {
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);

        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        newPet = petService.savePet(newPet);
        petService.deletePet(newPet);

        Long petId = newPet.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(petId));
    }

    @Test
    public void deleteAllPets() {
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);

        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        Pet newPet2 = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        petService.savePet(newPet);
        petService.savePet(newPet2);
        petService.deleteAllPets();

        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(1L));
        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(2L));
    }

    @Test
    public void updatePet() throws EntityNotFoundException {
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);

        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        newPet = petService.savePet(newPet);

        newPet.setName("dog");
        petService.updatePet(newPet);

        Assertions.assertEquals("dog", petService.getPetById(newPet.getId()).getName());

        petService.deleteAllPets();
    }

    @Test
    public void getAllPets() {
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        petService.deleteAllPets();


        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");

        Pet newPet2 = new Pet();
        newPet2.setName("cat2");
        newPet2.setColor(Colors.RED);
        newPet2.setBreed("fatCat");

        petService.savePet(newPet);
        petService.savePet(newPet2);

        List<Pet> pets = petService.getAllPets();


        Assertions.assertEquals("cat", pets.get(0).getName());
        Assertions.assertEquals("cat2", pets.get(1).getName());

        petService.deleteAllPets();
    }
}
