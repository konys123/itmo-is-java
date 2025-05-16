package lab3.services;

import jakarta.persistence.EntityNotFoundException;
import lab3.Exceptions.OwnerAlreadyExistException;
import lab3.dao.OwnerDao;
import lab3.dao.PetDao;
import lab3.dto.OwnerDto;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lab3.entities.Role;
import lab3.specifications.OwnerSpecifications;
import lombok.RequiredArgsConstructor;
import lab3.mapper.OwnerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Transactional
@Service("ownerService")
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerDao ownerDao;
    private final PetDao petDao;
    private final PasswordEncoder passwordEncoder;

    public OwnerDto saveOwner(OwnerDto ownerDto) {
        if (ownerDao.findByName(ownerDto.getName()) != null)
            throw new OwnerAlreadyExistException("имя " + ownerDto.getName() + " уже занято");
        Owner owner = OwnerMapper.toEntity(ownerDto);
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        owner.setRoles(Set.of(Role.USER));
        owner = ownerDao.save(owner);

        return OwnerMapper.toDto(owner);
    }

    @PreAuthorize("hasRole('ADMIN') or @ownerService.isAuthorized(#id, principal.username)")
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN') or @ownerService.isAuthorized(#ownerDto.id, principal.username)")
    public OwnerDto updateOwner(@P("ownerDto") OwnerDto ownerDto) {
        Owner existingOwner = ownerDao.findById(ownerDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));
        for (Pet pet : existingOwner.getPets()) {
            pet.setOwner(null);
        }


        Owner owner = OwnerMapper.toEntity(ownerDto);
        if (ownerDto.getPetIds() != null) owner.setPets(petDao.findAllById(ownerDto.getPetIds()));
        for (Pet pet : owner.getPets()) {
            if (pet.getOwner() != null && !pet.getOwner().getId().equals(owner.getId()))
                throw new AccessDeniedException("");
            pet.setOwner(owner);
        }

        owner.setPassword(passwordEncoder.encode(ownerDto.getPassword()));

        return OwnerMapper.toDto(ownerDao.save(owner));
    }

    @PreAuthorize("hasRole('ADMIN') or @ownerService.isAuthorized(#ownerDto.id, principal.username)")
    public OwnerDto modifyOwner(@P("ownerDto") OwnerDto ownerDto) {
        Owner owner = ownerDao.findById(ownerDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));
        Owner newOwner = OwnerMapper.toEntity(ownerDto);

        if (ownerDto.getPetIds() != null) newOwner.setPets(petDao.findAllById(ownerDto.getPetIds()));
        if (newOwner.getName() != null) owner.setName(newOwner.getName());
        if (newOwner.getPets() != null) owner.setPets(newOwner.getPets());
        if (newOwner.getBirthDate() != null) owner.setBirthDate(newOwner.getBirthDate());
        if (newOwner.getPets() != null) {
            for (Pet pet : newOwner.getPets()) {
                if (pet.getOwner() != null && !pet.getOwner().getId().equals(owner.getId()))
                    throw new AccessDeniedException("");
                pet.setOwner(owner);
            }
        }
        if (newOwner.getPassword() != null) owner.setPassword(passwordEncoder.encode(newOwner.getPassword()));

        return OwnerMapper.toDto(ownerDao.save(owner));

    }

    public boolean isAuthorized(Long id, String username) {
        Owner owner = ownerDao.findByName(username);
        return Objects.equals(owner.getId(), id);
    }

}
