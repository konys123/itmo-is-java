package entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "owners")
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pet> pets = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("%d %s %s", id, name, birthDate);
    }
}
