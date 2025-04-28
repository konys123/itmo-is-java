package lab3.mapper;

import jakarta.persistence.EntityNotFoundException;
import lab3.dao.OwnerDao;
import lab3.dao.PetDao;
import lab3.dto.OwnerDto;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class OwnerMapper {
    @Autowired
    protected PetDao petDao;
    @Autowired
    protected OwnerDao ownerDao;

    @Mapping(
            target = "petIds",
            expression = "java(owner.getPets().stream().map(lab3.entities.Pet::getId).collect(java.util.stream.Collectors.toList()))")
    public abstract OwnerDto toDto(Owner owner);

    @Mapping(target = "pets", expression = "java(resolvePets(ownerDto.getPetIds(),ownerDto.getId()))")
    public abstract Owner toEntity(OwnerDto ownerDto);

    protected List<Pet> resolvePets(List<Long> petIds, Long id) {
        if (petIds == null || petIds.isEmpty() || id == null) {
            return null;
        }

        Owner owner = ownerDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + id));
        List<Pet> pets = petDao.findAllById(petIds);

        for (Pet pet : pets) {
            pet.setOwner(owner);
        }

        return pets;
    }
}
