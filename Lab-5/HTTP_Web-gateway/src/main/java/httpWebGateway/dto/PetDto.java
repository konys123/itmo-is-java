package httpWebGateway.dto;

import httpWebGateway.entities.Colors;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private LocalDate birthDate;
    private Colors color;
    private Long ownerId;
    private List<Long> friendIds;
}
