package lab3.mapper;

import jakarta.persistence.EntityNotFoundException;
import lab3.dao.OwnerDao;
import lab3.dao.PetDao;
import lab3.dto.PetDto;
import lab3.entities.Owner;
import lab3.entities.Pet;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class PetMapper {
    @Autowired
    protected OwnerDao ownerDao;
    @Autowired
    protected PetDao petDao;

    @Mapping(source = "owner", target = "ownerId")
    @Mapping(
            target = "friendIds",
            expression = "java(pet.getFriends().stream().map(Pet::getId).collect(java.util.stream.Collectors.toList()))"
    )
    public abstract PetDto toDto(Pet pet);


    @Mapping(source = "ownerId", target = "owner")
    @Mapping(target = "friends", expression = "java(resolveFriends(dto.getFriendIds()))")
    public abstract Pet toEntity(PetDto dto);

    protected Long map(Owner owner){
        if (owner == null) return null;
        return owner.getId();
    }

    protected Owner map(Long ownerId) {
        if (ownerId == null) return null;
        return ownerDao.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("Owner не найден с id=" + ownerId));
    }

    protected List<Pet> resolveFriends(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }
        return petDao.findAllById(ids);
    }
}
