package lab3.dao;

import lab3.entities.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerDao extends JpaRepository<Owner, Long> {
    Page<Owner> findAll(Specification<Owner> spec, Pageable pageable);

    Owner findByName(String name);
}
