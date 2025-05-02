package lab3.services;

import jakarta.persistence.EntityNotFoundException;
import lab3.dao.OwnerDao;
import lab3.dao.PetDao;
import lab3.dto.OwnerDto;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lab3.specifications.OwnerSpecifications;
import lombok.RequiredArgsConstructor;
import lab3.mapper.OwnerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerDao ownerDao;
    private final PetDao petDao;

    public OwnerDto saveOwner(OwnerDto ownerDto) {
        Owner owner = ownerDao.save(OwnerMapper.toEntity(ownerDto));

        List<Pet> pets = petDao.findAllById(ownerDto.getPetIds());
        for (Pet pet : pets) {
            pet.setOwner(owner);
        }
        owner.setPets(pets);

        return OwnerMapper.toDto(owner);
    }

    public void deleteOwnerById(Long id) {
        Owner owner = ownerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + id));
        if (owner.getPets() != null) {
            for (Pet pet : owner.getPets()) {
                pet.setOwner(null);
            }
        }

        ownerDao.deleteById(id);
    }


    public void deleteAllOwners() {
        ownerDao.deleteAll();
    }

    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + id));

        return OwnerMapper.toDto(owner);
    }

    public Page<OwnerDto> getAllOwners(String name, LocalDate birthDate, Pageable pageable) {
        Specification<Owner> spec = Specification.where(null);
        if (name != null) {
            spec = spec.and(OwnerSpecifications.nameContains(name));
        }
        if (birthDate != null) {
            spec = spec.and(OwnerSpecifications.birthDateBefore(birthDate));
        }

        Page<Owner> owners = ownerDao.findAll(spec, pageable);
        return owners.map(OwnerMapper::toDto);
    }

    public OwnerDto updateOwner(OwnerDto ownerDto) {
        Owner existingOwner = ownerDao.findById(ownerDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));
        for (Pet pet : existingOwner.getPets()) {
            pet.setOwner(null);
        }


        Owner owner = OwnerMapper.toEntity(ownerDto);
        List<Pet> pets = petDao.findAllById(ownerDto.getPetIds());
        owner.setPets(pets);
        for (Pet pet : pets) {
            pet.setOwner(owner);
        }

        return OwnerMapper.toDto(ownerDao.save(owner));
    }

    public OwnerDto modifyOwner(OwnerDto ownerDto) {
        Owner owner = ownerDao.findById(ownerDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));
        Owner newOwner = OwnerMapper.toEntity(ownerDto);
        List<Pet> pets = petDao.findAllById(ownerDto.getPetIds());
        newOwner.setPets(pets);

        if (newOwner.getName() != null) owner.setName(newOwner.getName());
        if (newOwner.getPets() != null) owner.setPets(newOwner.getPets());
        if (newOwner.getBirthDate() != null) owner.setBirthDate(newOwner.getBirthDate());
        if (newOwner.getPets() != null) {
            for (Pet pet : newOwner.getPets()) {
                pet.setOwner(owner);
            }
        }

        return OwnerMapper.toDto(ownerDao.save(owner));

    }

}
