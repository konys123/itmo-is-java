import dao.OwnerDao;
import dao.PetDao;
import entities.Colors;
import entities.Owner;
import entities.Pet;
import exceptions.EntityNotFoundException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import services.OwnerService;
import services.PetService;

import java.util.List;


public class PetTests {
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15.2")
                    .withDatabaseName("test_db")
                    .withUsername("user")
                    .withPassword("password");

    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setup() {
        postgresContainer.start();
        Configuration configuration = new Configuration().configure("hibernate-docker.cfg.xml");
        configuration.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    public static void stop() {
        sessionFactory.close();
        postgresContainer.stop();
    }

    @Test
    public void savePet() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        OwnerService ownerService = new OwnerService(ownerDao);

        Owner owner = new Owner();
        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        ownerService.saveOwner(owner);
        newPet = petService.savePet(newPet);

        Assertions.assertEquals("cat", petService.getPetById(newPet.getId()).getName());
    }

    @Test
    public void deletePetById() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        OwnerService ownerService = new OwnerService(ownerDao);

        Owner owner = new Owner();
        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        ownerService.saveOwner(owner);
        newPet = petService.savePet(newPet);
        petService.deletePetById(newPet.getId());

        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(1L));
    }

    @Test
    public void deletePetByEntity() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        OwnerService ownerService = new OwnerService(ownerDao);

        Owner owner = new Owner();
        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        ownerService.saveOwner(owner);
        newPet = petService.savePet(newPet);
        petService.deletePet(newPet);

        Long petId = newPet.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(petId));
    }

    @Test
    public void deleteAllPets() {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        OwnerService ownerService = new OwnerService(ownerDao);

        Owner owner = new Owner();
        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        Pet newPet2 = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        ownerService.saveOwner(owner);
        petService.savePet(newPet);
        petService.savePet(newPet2);
        petService.deleteAllPets();

        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(1L));
        Assertions.assertThrows(EntityNotFoundException.class, () -> petService.getPetById(2L));
    }

    @Test
    public void updatePet() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        OwnerService ownerService = new OwnerService(ownerDao);

        Owner owner = new Owner();
        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        ownerService.saveOwner(owner);
        newPet = petService.savePet(newPet);

        newPet.setName("dog");
        petService.updatePet(newPet);

        Assertions.assertEquals("dog", petService.getPetById(newPet.getId()).getName());
    }

    @Test
    public void getAllPets() {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);
        PetDao petDao = new PetDao(sessionFactory);

        PetService petService = new PetService(petDao);
        OwnerService ownerService = new OwnerService(ownerDao);
        petService.deleteAllPets();

        Owner owner = new Owner();
        owner = ownerService.saveOwner(owner);

        Pet newPet = new Pet();
        newPet.setName("cat");
        newPet.setColor(Colors.RED);
        newPet.setBreed("fatCat");
        newPet.setOwner(owner);

        Pet newPet2 = new Pet();
        newPet2.setName("cat2");
        newPet2.setColor(Colors.RED);
        newPet2.setBreed("fatCat");
        newPet2.setOwner(owner);

        petService.savePet(newPet);
        petService.savePet(newPet2);

        List<Pet> pets = petService.getAllPets();


        Assertions.assertEquals("cat", pets.get(0).getName());
        Assertions.assertEquals("cat2", pets.get(1).getName());
    }
}
