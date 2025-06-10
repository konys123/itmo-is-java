package httpWebGateway.dao;

import httpWebGateway.entities.Owner;
import org.hibernate.SessionFactory;

public class OwnerDao extends AbstractDao<Owner> {

    public OwnerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
