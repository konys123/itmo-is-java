package petsApp.specifications;

import petsApp.entities.Colors;
import petsApp.entities.Pet;
import org.springframework.data.jpa.domain.Specification;

public class PetSpecifications {
    public static Specification<Pet> hasColor(Colors color) {
        return (root, query, cb) ->
                cb.equal(root.get("color"), color);
    }

    public static Specification<Pet> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Pet> hasBreed(String breed) {
        return (root, query, cb) ->
                cb.equal(root.get("breed"), breed);
    }
}
