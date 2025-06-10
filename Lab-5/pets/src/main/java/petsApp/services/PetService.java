package petsApp.services;

import petsApp.Exceptions.ForbiddenException;
import petsApp.entities.Role;
import jakarta.persistence.EntityNotFoundException;
import petsApp.dao.PetDao;
import petsApp.dto.PetDto;
import petsApp.entities.Colors;
import petsApp.entities.Pet;
import petsApp.mapper.PetMapper;
import petsApp.specifications.PetSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetDao petDao;

    public PetDto savePet(PetDto petDto) {
        Pet pet = PetMapper.toEntity(petDto);
        if (petDto.getOwnerId() != null) pet.setOwnerId(petDto.getOwnerId());
        if (petDto.getFriendIds() != null) pet.setFriends(petDao.findAllById(petDto.getFriendIds()));

        return PetMapper.toDto(petDao.save(pet));
    }

    public void deletePetById(Long id, Long ownerId, Set<Role> roles) {
        isPetOwner(id, ownerId, roles);
        petDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + id));
        petDao.deleteById(id);
    }

    public void deleteAllPets(Set<Role> roles) {
        if (!roles.contains(Role.ADMIN)) throw new ForbiddenException("доступ запрещен");
        petDao.deleteAll();
    }

    public PetDto getPetById(Long id) {
        Pet pet = petDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + id));
        return PetMapper.toDto(pet);
    }

    public List<PetDto> getPetByOwnerId(Long id) {
        List<Pet> pets = petDao.findAllByOwnerId(id);
        List<PetDto> petDtos = new ArrayList<>();
        for (Pet pet : pets){
            petDtos.add(PetMapper.toDto(pet));
        }
        return petDtos;
    }

    public Page<PetDto> getAllPets(Colors color, String name, String breed, Pageable pageable) {
        Specification<Pet> spec = Specification.where(null);
        if (color != null) {
            spec = spec.and(PetSpecifications.hasColor(color));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(PetSpecifications.nameContains(name));
        }
        if (breed != null && !breed.isBlank()) {
            spec = spec.and(PetSpecifications.hasBreed(breed));
        }

        Page<Pet> pets = petDao.findAll(spec, pageable);
        return pets.map(PetMapper::toDto);
    }

    public PetDto updatePet(PetDto petDto, Long ownerId, Set<Role> roles) {
        isPetOwner(petDto.getId(),ownerId, roles);
        petDao.findById(petDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));

        Pet pet = PetMapper.toEntity(petDto);
        pet.setOwnerId(petDto.getOwnerId());
        pet.setFriends(petDao.findAllById(petDto.getFriendIds()));

        return PetMapper.toDto(petDao.save(pet));
    }

    public PetDto modifyPet(PetDto petDto, Long ownerId, Set<Role> roles) {
        isPetOwner(petDto.getId(),ownerId, roles);
        Pet pet = petDao.findById(petDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));

        Pet newPet = PetMapper.toEntity(petDto);
        if (petDto.getOwnerId() != null) newPet.setOwnerId(petDto.getOwnerId());
        if (petDto.getFriendIds() != null) newPet.setFriends(petDao.findAllById(petDto.getFriendIds()));

        if (newPet.getName() != null) pet.setName(newPet.getName());
        if (newPet.getBirthDate() != null) pet.setBirthDate(newPet.getBirthDate());
        if (newPet.getBreed() != null) pet.setBreed(newPet.getBreed());
        if (newPet.getColor() != null) pet.setColor(newPet.getColor());
        if (newPet.getOwnerId() != null) pet.setOwnerId(newPet.getOwnerId());
        if (newPet.getFriends() != null) pet.setFriends(newPet.getFriends());

        return PetMapper.toDto(petDao.save(pet));
    }

    public void isPetOwner(Long petId, Long ownerId, Set<Role> roles) {
        Pet pet = petDao.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petId));

        if (roles.contains(Role.ADMIN)) return;

        if (pet.getOwnerId() != null & pet.getOwnerId() != ownerId)
            throw new ForbiddenException("доступ запрещен");
    }
}
