package services;

import dao.OwnerDao;
import entities.Owner;
import exceptions.EntityNotFoundException;

import java.util.List;

public class OwnerService {
    private final OwnerDao ownerDao;

    public OwnerService(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    public Owner saveOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    public void deleteOwnerById(Long id) throws EntityNotFoundException {
        ownerDao.deleteById(id);
    }

    public void deleteOwner(Owner owner) throws EntityNotFoundException {
        ownerDao.deleteByEntity(owner);
    }

    public void deleteAllOwners() {
        ownerDao.deleteAll();
    }

    public Owner updateOwner(Owner owner) throws EntityNotFoundException {
        return ownerDao.update(owner);
    }

    public Owner getOwnerById(Long id) throws EntityNotFoundException {
        return ownerDao.getById(id);
    }

    public List<Owner> getAllOwners() {
        return ownerDao.getAll();
    }
}
