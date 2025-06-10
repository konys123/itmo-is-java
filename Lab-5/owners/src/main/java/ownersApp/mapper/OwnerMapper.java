package ownersApp.mapper;

import ownersApp.dto.OwnerDto;
import ownersApp.entities.Owner;


public class OwnerMapper {
    public static OwnerDto toDto(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setBirthDate(owner.getBirthDate());
        dto.setUserId(owner.getUserId());
        return dto;
    }

    public static Owner toEntity(OwnerDto dto) {
        Owner owner = new Owner();
        owner.setId(dto.getId());
        owner.setName(dto.getName());
        owner.setBirthDate(dto.getBirthDate());
        owner.setUserId(dto.getUserId());
        return owner;
    }
}
