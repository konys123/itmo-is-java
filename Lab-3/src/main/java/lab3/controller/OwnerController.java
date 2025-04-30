package lab3.controller;

import lab3.dto.OwnerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lab3.services.OwnerService;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("/all")
    public ResponseEntity<Page<OwnerDto>> list(
            @RequestParam(name = "name", required = false) Optional<String> name,
            @RequestParam(name = "birthDate", required = false) Optional<LocalDate> birthDate,
            Pageable pageable) {
        return ResponseEntity.ok(ownerService.getAllOwners(name.orElse(null), birthDate.orElse(null), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwner(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ownerService.getOwnerById(id));
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

    @PostMapping
    public ResponseEntity<OwnerDto> saveOwner(@RequestBody OwnerDto ownerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.saveOwner(ownerDto));
    }

    @PutMapping
    public ResponseEntity<OwnerDto> updateOwner(@RequestBody OwnerDto ownerDto) {
        return ResponseEntity.status(HttpStatus.OK).body(ownerService.updateOwner(ownerDto));
    }

    @PatchMapping
    public ResponseEntity<OwnerDto> modifyOwner(@RequestBody OwnerDto ownerDto) {
        return ResponseEntity.status(HttpStatus.OK).body(ownerService.modifyOwner(ownerDto));
    }
}
