package services;

import dao.OwnerDao;
import entities.Owner;

import java.util.List;

public class OwnerService {
    private final OwnerDao ownerDao = new OwnerDao();

    public Owner saveOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    public void deleteOwnerById(Long id) {
        ownerDao.deleteById(id);
    }

    public void deleteOwner(Owner owner) {
        ownerDao.deleteByEntity(owner);
    }

    public void deleteAllOwners() {
        ownerDao.deleteAll();
    }

    public Owner updateOwner(Owner owner) {
        return ownerDao.update(owner);
    }

    public Owner getOwnerById(Long id) {
        return ownerDao.getById(id);
    }

    public List<Owner> getAllOwners() {
        return ownerDao.getAll();
    }
}
