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
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final OwnerDao ownerDao;
    @Autowired
    private final PetDao petDao;
    @Autowired
    private final OwnerMapper ownerMapper;

    public OwnerDto saveOwner(OwnerDto ownerDto) {
        Owner owner = ownerDao.save(ownerMapper.toEntity(ownerDto));

        List<Pet> pets = petDao.findAllById(ownerDto.getPetIds());
        for (Pet pet : pets) {
            pet.setOwner(owner);
        }
        owner.setPets(pets);

        return ownerMapper.toDto(owner);
    }

    public void deleteOwnerById(Long id) {
        ownerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + id));

        ownerDao.deleteById(id);
    }

    public void deleteOwner(OwnerDto ownerDto) {
        ownerDao.findById(ownerDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));

        ownerDao.delete(ownerMapper.toEntity(ownerDto));
    }

    public void deleteAllOwners() {
        ownerDao.deleteAll();
    }

    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + id));

        return ownerMapper.toDto(owner);
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
        return owners.map(ownerMapper::toDto);
    }

    public OwnerDto updateOwner(OwnerDto ownerDto) {
        ownerDao.findById(ownerDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));

        return ownerMapper.toDto(ownerDao.save(ownerMapper.toEntity(ownerDto)));
    }

    public OwnerDto modifyOwner(OwnerDto ownerDto) {
        Owner owner = ownerDao.findById(ownerDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));
        Owner newOwner = ownerMapper.toEntity(ownerDto);

        if (newOwner.getName() != null) owner.setName(newOwner.getName());
        if (newOwner.getPets() != null) owner.setPets(newOwner.getPets());
        if (newOwner.getBirthDate() != null) owner.setBirthDate(newOwner.getBirthDate());

        return ownerMapper.toDto(ownerDao.save(owner));

    }

}
