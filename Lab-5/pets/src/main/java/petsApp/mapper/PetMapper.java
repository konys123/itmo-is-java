package petsApp.mapper;

import petsApp.dto.PetDto;
import petsApp.entities.Pet;


public class PetMapper {

    public static PetDto toDto(Pet pet) {
        PetDto petDto = new PetDto();
        if (pet.getId() != null) petDto.setId(pet.getId());
        if (pet.getName() != null) petDto.setName(pet.getName());
        if (pet.getColor() != null) petDto.setColor(pet.getColor());
        if (pet.getBirthDate() != null) petDto.setBirthDate(pet.getBirthDate());
        if (pet.getBreed() != null) petDto.setBreed(pet.getBreed());
        if (pet.getOwnerId() != null) petDto.setOwnerId(pet.getOwnerId());
        if (pet.getFriends() != null)
            petDto.setFriendIds(pet.getFriends().stream().map(Pet::getId).collect(java.util.stream.Collectors.toList()));
        if (pet.getOwnerId() != null) petDto.setOwnerId(pet.getOwnerId());
        return petDto;
    }


    public static Pet toEntity(PetDto petDto) {
        Pet pet = new Pet();
        if (petDto.getId() != null) pet.setId(petDto.getId());
        if (petDto.getColor() != null) pet.setColor(petDto.getColor());
        if (petDto.getName() != null) pet.setName(petDto.getName());
        if (petDto.getBirthDate() != null) pet.setBirthDate(petDto.getBirthDate());
        if (petDto.getBreed() != null) pet.setBreed(petDto.getBreed());
        if (petDto.getOwnerId() != null) pet.setOwnerId(petDto.getOwnerId());
        return pet;
    }
}
