package lab3.mapper;

import lab3.dto.PetDto;
import lab3.entities.Pet;


public class PetMapper {

    public static PetDto toDto(Pet pet) {
        PetDto petDto = new PetDto();
        if (pet.getId() != null) petDto.setId(pet.getId());
        if (pet.getName() != null) petDto.setName(pet.getName());
        if (pet.getColor() != null) petDto.setColor(pet.getColor());
        if (pet.getBreed() != null) petDto.setBreed(pet.getBreed());
        if (pet.getOwner() != null) petDto.setOwnerId(pet.getOwner().getId());
        if (pet.getFriends() != null)
            petDto.setFriendIds(pet.getFriends().stream().map(Pet::getId).collect(java.util.stream.Collectors.toList()));
        return petDto;
    }


    public static Pet toEntity(PetDto petDto) {
        Pet pet = new Pet();
        if (petDto.getId() != null) pet.setId(petDto.getId());
        if (petDto.getColor() != null) pet.setColor(petDto.getColor());
        if (petDto.getName() != null) pet.setName(petDto.getName());
        if (petDto.getBirthDate() != null) pet.setBirthDate(petDto.getBirthDate());
        if (petDto.getBreed() != null) pet.setBreed(petDto.getBreed());
        return pet;
    }
}
