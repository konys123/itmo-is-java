package httpWebGateway.controller;

import httpWebGateway.config.KafkaConfig;
import httpWebGateway.dto.PetDto;
import httpWebGateway.dto.PetRequest;
import httpWebGateway.dto.PetResponse;
import httpWebGateway.entities.Colors;
import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import httpWebGateway.security.MyUserDetails;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pets")
public class PetController {

    private final ReplyingKafkaTemplate<String, PetRequest, PetResponse> kafka;

    @GetMapping("/all")
    public ResponseEntity<Page<PetDto>> list(
            @RequestParam(name = "color", required = false) Colors color,
            @RequestParam(name = "breed", required = false) String breed,
            @RequestParam(name = "name", required = false) String name,
            Pageable pageable
    ) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        PetDto petDto = new PetDto();
        petDto.setName(name);
        petDto.setColor(color);
        petDto.setBreed(breed);
        petRequest.setPetDto(petDto);
        petRequest.setPage(pageable.getPageNumber());
        petRequest.setSize(pageable.getPageSize());
        petRequest.setAction("GET/all");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        Page<PetDto> page = new PageImpl<>(
                resp.getPetDtos(),
                PageRequest.of(resp.getPageNumber(), resp.getPageSize()),
                resp.getTotalElements()
                );
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPet(@PathVariable("id") Long id) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setPetId(id);
        petRequest.setAction("GET/id");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        return ResponseEntity.ok(resp.getPetDto());
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllPets(Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setOwnerId(((MyUserDetails) auth.getPrincipal()).getId());
        petRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());
        petRequest.setAction("DELETE/all");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable("id") Long id, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setPetId(id);
        petRequest.setOwnerId(((MyUserDetails) auth.getPrincipal()).getId());
        petRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());
        petRequest.setAction("DELETE/id");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PetDto> savePet(@RequestBody PetDto petDto, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setPetDto(petDto);
        petRequest.setOwnerId(((MyUserDetails) auth.getPrincipal()).getId());
        petRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());
        petRequest.setAction("POST");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resp.getPetDto());
    }

    @PutMapping
    public ResponseEntity<PetDto> updatePet(@RequestBody PetDto petDto, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setPetDto(petDto);
        petRequest.setOwnerId(((MyUserDetails) auth.getPrincipal()).getId());
        petRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());
        petRequest.setAction("PUT");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        return ResponseEntity.ok(resp.getPetDto());
    }

    @PatchMapping
    public ResponseEntity<PetDto> modifyPet(@RequestBody PetDto petDto, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setPetDto(petDto);
        petRequest.setOwnerId(((MyUserDetails) auth.getPrincipal()).getId());
        petRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());
        petRequest.setAction("PATCH");

        PetResponse resp = waitResponse(petRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        return ResponseEntity.ok(resp.getPetDto());
    }

    private PetResponse waitResponse(PetRequest petRequest) throws ExecutionException, InterruptedException, TimeoutException {
        String requestTopic = KafkaConfig.PET_REQUESTS;
        long replyTimeoutMs = 5_000;
        RequestReplyFuture<String, PetRequest, PetResponse> future =
                kafka.sendAndReceive(
                        new ProducerRecord<>(requestTopic, null, petRequest),
                        Duration.ofMillis(replyTimeoutMs)
                );

        ConsumerRecord<String, PetResponse> consumerRecord = future.get(replyTimeoutMs, TimeUnit.MILLISECONDS);
        return consumerRecord.value();
    }
}