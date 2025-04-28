package lab3.specifications;

import lab3.entities.Owner;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class OwnerSpecifications {
    public static Specification<Owner> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Owner> birthDateBefore(LocalDate birthDate) {
        return (root, query, cb) ->
                cb.lessThan(root.get("birthDate"), birthDate);
    }
}
