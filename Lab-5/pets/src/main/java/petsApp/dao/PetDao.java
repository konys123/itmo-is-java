package petsApp.dao;

import petsApp.entities.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PetDao extends JpaRepository<Pet, Long> {
    Page<Pet> findAll(Specification<Pet> specification, Pageable pageable);

    List<Pet> findAllByOwnerId(Long ownerId);
}
