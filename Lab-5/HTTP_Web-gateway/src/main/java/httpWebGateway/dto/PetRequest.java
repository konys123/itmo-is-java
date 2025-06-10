package httpWebGateway.dto;

import httpWebGateway.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
public class PetRequest {
    private String correlationId;
    private Long ownerId;
    private Long petId;
    private Set<Role> userRoles;
    private PetDto petDto;
    private Integer page;
    private Integer size;
    private String action;
}
