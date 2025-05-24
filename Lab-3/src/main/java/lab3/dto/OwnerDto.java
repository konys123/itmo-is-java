package lab3.dto;

import lab3.entities.Role;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class OwnerDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private List<Long> petIds;
    private Set<Role> roles;
    private String password;
}
