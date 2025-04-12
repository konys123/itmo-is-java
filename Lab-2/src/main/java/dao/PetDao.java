package dao;

import entities.Pet;
import org.hibernate.SessionFactory;

public class PetDao extends AbstractDao<Pet> {

    public PetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
