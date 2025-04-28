package lab3.controller;

import lab3.dto.OwnerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lab3.services.OwnerService;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerController {
    @Autowired
    private final OwnerService ownerService;

    @GetMapping("/all")
    public Page<OwnerDto> list(
            @RequestParam(name = "name", required = false) Optional<String> name,
            @RequestParam(name = "birthDate", required = false) Optional<LocalDate> birthDate,
            Pageable pageable) {
        return ownerService.getAllOwners(name.orElse(null), birthDate.orElse(null), pageable);
    }

    @GetMapping("/{id}")
    public OwnerDto getOwner(@PathVariable("id") Long id) {
        return ownerService.getOwnerById(id);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllOwners() {
        ownerService.deleteAllOwners();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwnerById(@PathVariable("id") Long id) {
        ownerService.deleteOwnerById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteOwner(@RequestBody OwnerDto ownerDto) {
        ownerService.deleteOwner(ownerDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public OwnerDto saveOwner(@RequestBody OwnerDto ownerDto) {
        return ownerService.saveOwner(ownerDto);
    }

    @PutMapping
    public OwnerDto updateOwner(@RequestBody OwnerDto ownerDto) {
        return ownerService.updateOwner(ownerDto);
    }

    @PatchMapping
    public OwnerDto modifyOwner(@RequestBody OwnerDto ownerDto) {
        return ownerService.modifyOwner(ownerDto);
    }
}
