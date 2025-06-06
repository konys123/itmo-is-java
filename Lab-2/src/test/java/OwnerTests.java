import dao.OwnerDao;
import entities.Owner;
import exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.OwnerService;


import java.util.List;


public class OwnerTests extends AbstractTest {

    @Test
    public void saveOwner() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);

        OwnerService ownerService = new OwnerService(ownerDao);

        Owner newOwner = new Owner();
        newOwner.setName("ньюовнер");

        newOwner = ownerService.saveOwner(newOwner);

        Assertions.assertEquals("ньюовнер", ownerService.getOwnerById(newOwner.getId()).getName());

        ownerService.deleteAllOwners();
    }

    @Test
    public void deleteOwnerById() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);

        OwnerService ownerService = new OwnerService(ownerDao);

        Owner newOwner = new Owner();
        newOwner.setName("ньюовнер");

        newOwner = ownerService.saveOwner(newOwner);
        ownerService.deleteOwnerById(newOwner.getId());

        Long id = newOwner.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> ownerService.getOwnerById(id));
    }

    @Test
    public void deleteOwnerByEntity() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);

        OwnerService ownerService = new OwnerService(ownerDao);

        Owner newOwner = new Owner();
        newOwner.setName("ньюовнер");

        newOwner = ownerService.saveOwner(newOwner);

        ownerService.deleteOwner(newOwner);

        Long id = newOwner.getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> ownerService.getOwnerById(id));
    }

    @Test
    public void deleteAllOwners() {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);

        OwnerService ownerService = new OwnerService(ownerDao);

        Owner newOwner = new Owner();
        newOwner.setName("ньюовнер");

        Owner newOwner2 = new Owner();
        newOwner2.setName("ньюовнер2");

        ownerService.saveOwner(newOwner);
        ownerService.saveOwner(newOwner2);
        ownerService.deleteAllOwners();

        Assertions.assertThrows(EntityNotFoundException.class, () -> ownerService.getOwnerById(newOwner.getId()));
        Assertions.assertThrows(EntityNotFoundException.class, () -> ownerService.getOwnerById(newOwner2.getId()));
    }

    @Test
    public void updateOwner() throws EntityNotFoundException {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);

        OwnerService ownerService = new OwnerService(ownerDao);

        Owner newOwner = new Owner();
        newOwner.setName("ньюовнер");

        newOwner = ownerService.saveOwner(newOwner);

        newOwner.setName("олдовнер");
        ownerService.updateOwner(newOwner);

        Assertions.assertEquals("олдовнер", ownerService.getOwnerById(newOwner.getId()).getName());

        ownerService.deleteAllOwners();
    }

    @Test
    public void getAllOwners() {
        OwnerDao ownerDao = new OwnerDao(sessionFactory);

        OwnerService ownerService = new OwnerService(ownerDao);
        ownerService.deleteAllOwners();

        Owner newOwner = new Owner();
        newOwner.setName("ньюовнер");

        Owner newOwner2 = new Owner();
        newOwner2.setName("ньюовнер2");

        ownerService.saveOwner(newOwner);
        ownerService.saveOwner(newOwner2);

        List<Owner> owners = ownerService.getAllOwners();

        System.out.println(owners);

        Assertions.assertEquals("ньюовнер", owners.get(0).getName());
        Assertions.assertEquals("ньюовнер2", owners.get(1).getName());

        ownerService.deleteAllOwners();
    }
}
