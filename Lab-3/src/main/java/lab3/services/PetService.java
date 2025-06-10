package lab3.services;

import jakarta.persistence.EntityNotFoundException;
import lab3.dao.OwnerDao;
import lab3.dao.PetDao;
import lab3.dto.PetDto;
import lab3.entities.Colors;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lab3.mapper.PetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import lab3.specifications.PetSpecifications;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetDao petDao;
    private final OwnerDao ownerDao;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PetDto savePet(PetDto petDto) {
        Pet pet = PetMapper.toEntity(petDto);
        if (petDto.getOwnerId() != null) pet.setOwner(ownerDao.findById(petDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + petDto.getOwnerId())));
        if (petDto.getFriendIds() != null) pet.setFriends(petDao.findAllById(petDto.getFriendIds()));

        return PetMapper.toDto(petDao.save(pet));
    }

    @PreAuthorize("hasRole('ADMIN') or @petService.isPetOwner(#id, principal.username)")
    public void deletePetById(Long id) {
        petDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + id));
        petDao.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllPets() {
        petDao.deleteAll();
    }

    public PetDto getPetById(Long id) {
        Pet pet = petDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + id));
        return PetMapper.toDto(pet);
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

    @PreAuthorize("hasRole('ADMIN') or @petService.isPetOwner(#petDto.id, principal.username)")
    public PetDto updatePet(@P("petDto") PetDto petDto) {
        petDao.findById(petDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));

        Pet pet = PetMapper.toEntity(petDto);
        pet.setOwner(ownerDao.findById(petDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + petDto.getOwnerId())));
        pet.setFriends(petDao.findAllById(petDto.getFriendIds()));

        return PetMapper.toDto(petDao.save(pet));
    }

    @PreAuthorize("hasRole('ADMIN') or @petService.isPetOwner(#petDto.id, principal.username)")
    public PetDto modifyPet(@P("petDto") PetDto petDto) {
        Pet pet = petDao.findById(petDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petDto.getId()));

        Pet newPet = PetMapper.toEntity(petDto);
        if (petDto.getOwnerId() != null) newPet.setOwner(ownerDao.findById(petDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + petDto.getOwnerId())));
        if (petDto.getFriendIds() != null) newPet.setFriends(petDao.findAllById(petDto.getFriendIds()));

        if (newPet.getName() != null) pet.setName(newPet.getName());
        if (newPet.getBirthDate() != null) pet.setBirthDate(newPet.getBirthDate());
        if (newPet.getBreed() != null) pet.setBreed(newPet.getBreed());
        if (newPet.getColor() != null) pet.setColor(newPet.getColor());
        if (newPet.getOwner() != null) pet.setOwner(newPet.getOwner());
        if (newPet.getFriends() != null) pet.setFriends(newPet.getFriends());

        return PetMapper.toDto(petDao.save(pet));
    }

    public boolean isPetOwner(Long petId, String username) {
        Pet pet = petDao.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet не найден с id=" + petId));
        Owner owner = ownerDao.findByName(username);
        return pet.getOwner().equals(owner);
    }
}
