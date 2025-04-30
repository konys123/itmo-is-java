package lab3.controller;

import lab3.dto.PetDto;
import lab3.entities.Colors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lab3.services.PetService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;

    @GetMapping("/all")
    public Page<PetDto> list(
            @RequestParam(name = "color", required = false) Optional<Colors> color,
            @RequestParam(name = "breed", required = false) Optional<String> breed,
            @RequestParam(name = "name", required = false) Optional<String> name,
            Pageable pageable
    ) {
        return petService.
                getAllPets(color.orElse(null), name.orElse(null),
                        breed.orElse(null), pageable);
    }

    @GetMapping("/{id}")
    public PetDto getPet(@PathVariable("id") Long id) {
        return petService.getPetById(id);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllPets() {
        petService.deleteAllPets();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable("id") Long id) {
        petService.deletePetById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public PetDto savePet(@RequestBody PetDto petDto) {
        return petService.savePet(petDto);
    }

    @PutMapping
    public PetDto updatePet(@RequestBody PetDto petDto) {
        return petService.updatePet(petDto);
    }

    @PatchMapping
    public PetDto modifyPet(@RequestBody PetDto petDto) {
        return petService.modifyPet(petDto);
    }
}