package ownersApp.services;

import ownersApp.Exceptions.ForbiddenException;
import ownersApp.dao.OwnerDao;
import jakarta.persistence.EntityNotFoundException;

import ownersApp.dto.OwnerDto;
import ownersApp.entities.Owner;
import ownersApp.entities.Role;
import ownersApp.mapper.OwnerMapper;
import ownersApp.specifications.OwnerSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Transactional
@Service("ownerService")
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerDao ownerDao;

    public OwnerDto saveOwner(OwnerDto ownerDto) {
        Owner owner = OwnerMapper.toEntity(ownerDto);
        owner = ownerDao.save(owner);

        return OwnerMapper.toDto(owner);
    }

    public void deleteOwnerById(Long id, Long userId, Set<Role> roles) {
        isAuthorized(id, userId, roles);
        ownerDao.deleteById(id);
    }

    public void deleteAllOwners(Set<Role> roles) {
        if (!roles.contains(Role.ADMIN)) throw new ForbiddenException("Сосал?");
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

    public OwnerDto updateOwner(OwnerDto ownerDto, Set<Role> roles) {
        isAuthorized(ownerDto.getId(), ownerDto.getUserId(), roles);

        Owner owner = OwnerMapper.toEntity(ownerDto);

        return OwnerMapper.toDto(ownerDao.save(owner));
    }

    public OwnerDto modifyOwner(OwnerDto ownerDto, Set<Role> roles) {
        isAuthorized(ownerDto.getId(), ownerDto.getUserId(), roles);

        Owner owner = ownerDao.findById(ownerDto.getId()).
                orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerDto.getId()));

        Owner newOwner = OwnerMapper.toEntity(ownerDto);
        if (newOwner.getName() != null) owner.setName(newOwner.getName());
        if (newOwner.getBirthDate() != null) owner.setBirthDate(newOwner.getBirthDate());

        return OwnerMapper.toDto(ownerDao.save(owner));

    }

    public void isAuthorized(Long id, Long userId, Set<Role> roles) {
        Owner owner = ownerDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + id));

        if (roles.contains(Role.ADMIN)) return;

        if (owner.getUserId() != null & owner.getUserId() != userId) throw new ForbiddenException("Доступ запрещен");
    }

}
