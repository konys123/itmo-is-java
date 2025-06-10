package lab3.dao;

import lab3.entities.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PetDao extends JpaRepository<Pet, Long> {
    Page<Pet> findAll(Specification<Pet> specification, Pageable pageable);
}
