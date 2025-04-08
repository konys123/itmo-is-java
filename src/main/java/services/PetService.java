package services;

import repositories.OwnerDao;
import repositories.PetDao;

public class PetService<T> {
    private final PetDao petDao = new PetDao();
    private final OwnerDao ownerDao = new OwnerDao();
}
