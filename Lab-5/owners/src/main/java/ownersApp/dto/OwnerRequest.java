package ownersApp.dto;

import ownersApp.entities.Role;
import lombok.Data;

import java.util.Set;

@Data
public class OwnerRequest {
    private String correlationId;
    private Long ownerId;
    private Long userId;
    private Set<Role> userRoles;
    private OwnerDto ownerDto;
    private Integer page;
    private Integer size;
    private String action;
}
