package lab3.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OwnerDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private List<Long> petIds;
}
