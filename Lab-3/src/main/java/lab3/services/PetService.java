package lab3.services;

import jakarta.persistence.EntityNotFoundException;
import lab3.dao.OwnerDao;
import lab3.dao.PetDao;
import lab3.dto.PetDto;
import lab3.entities.Colors;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lombok.RequiredArgsConstructor;
import lab3.mapper.PetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lab3.specifications.PetSpecifications;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PetService {
    @Autowired
    private final PetDao petDao;
    @Autowired
    private final OwnerDao ownerDao;
    @Autowired
    private final PetMapper petMapper;


    public PetDto savePet(PetDto pet) {
        return petMapper.toDto(petDao.save(petMapper.toEntity(pet)));
    }

    public void deletePetById(Long id) {
        petDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + id));
        petDao.deleteById(id);
    }

    public void deletePet(PetDto petDto) {
        petDao.findById(petDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));
        petDao.delete(petMapper.toEntity(petDto));
    }

    public void deleteAllPets() {
        petDao.deleteAll();
    }

    public PetDto getPetById(Long id) {
        Pet pet = petDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + id));
        return petMapper.toDto(pet);
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
        return pets.map(petMapper::toDto);
    }

    public PetDto updatePet(PetDto petDto) {
        petDao.findById(petDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));

        return petMapper.toDto(petDao.save(petMapper.toEntity(petDto)));
    }

    public PetDto modifyPet(PetDto petDto) {
        Pet pet = petDao.findById(petDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));
        Pet newPet = petMapper.toEntity(petDto);
        if (newPet.getName() != null) pet.setName(newPet.getName());
        if (newPet.getBirthDate() != null) pet.setBirthDate(newPet.getBirthDate());
        if (newPet.getBreed() != null) pet.setBreed(newPet.getBreed());
        if (newPet.getColor() != null) pet.setColor(newPet.getColor());
        if (newPet.getOwner() != null) pet.setOwner(newPet.getOwner());
        if (newPet.getFriends() != null) pet.setFriends(newPet.getFriends());

        return petMapper.toDto(petDao.save(pet));

    }
}
