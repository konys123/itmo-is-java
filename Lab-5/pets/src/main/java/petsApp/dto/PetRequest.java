package petsApp.dto;

import petsApp.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
public class PetRequest {
    private String correlationId;
    private Long ownerId;
    private Set<Role> userRoles;
    private Long petId;
    private PetDto petDto;
    private Integer page;
    private Integer size;
    private String action;
}
