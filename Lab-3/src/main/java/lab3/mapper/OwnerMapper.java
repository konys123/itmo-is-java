package lab3.mapper;

import lab3.dao.PetDao;
import lab3.dto.OwnerDto;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class OwnerMapper {
    private final PetDao petDao;


    public OwnerDto toDto(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setBirthDate(owner.getBirthDate());
        dto.setPetIds(owner.getPets().stream().map(Pet::getId).toList());
        return dto;
    }

    public Owner toEntity(OwnerDto dto) {
        Owner owner = new Owner();
        owner.setId(dto.getId());
        owner.setName(dto.getName());
        owner.setBirthDate(dto.getBirthDate());
        if (dto.getPetIds() != null) {
            owner.setPets(petDao.findAllById(dto.getPetIds()));
        }
        return owner;
    }
}
