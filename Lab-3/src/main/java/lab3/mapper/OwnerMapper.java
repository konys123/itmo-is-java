package lab3.mapper;

import lab3.dto.OwnerDto;
import lab3.entities.Owner;
import lab3.entities.Pet;


public class OwnerMapper {
    public static OwnerDto toDto(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setBirthDate(owner.getBirthDate());
        dto.setPetIds(owner.getPets().stream().map(Pet::getId).toList());
        return dto;
    }

    public static Owner toEntity(OwnerDto dto) {
        Owner owner = new Owner();
        owner.setId(dto.getId());
        owner.setName(dto.getName());
        owner.setBirthDate(dto.getBirthDate());
        return owner;
    }
}
