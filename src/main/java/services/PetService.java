package services;

import entities.Owner;
import entities.Pet;
import repositories.PostgresOwnerRepository;
import repositories.PostgresPetRepository;

public class PetService<T> {
    private final PostgresPetRepository postgresPetRepository = new PostgresPetRepository();
    private final PostgresOwnerRepository postgresOwnerRepository = new PostgresOwnerRepository();
}
