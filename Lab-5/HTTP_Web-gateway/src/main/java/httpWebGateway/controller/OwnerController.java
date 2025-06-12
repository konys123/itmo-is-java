package httpWebGateway.controller;

import httpWebGateway.config.KafkaConfig;
import httpWebGateway.dto.*;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerController {

    private final ReplyingKafkaTemplate<String, OwnerRequest, OwnerResponse> ownerKafka;
    private final ReplyingKafkaTemplate<String, PetRequest, PetResponse> petKafka;

    @GetMapping("/all")
    public ResponseEntity<Page<OwnerDto>> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "birthDate", required = false) LocalDate birthDate,
            Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(name);
        ownerDto.setBirthDate(birthDate);
        ownerRequest.setOwnerDto(ownerDto);
        ownerRequest.setAction("GET/all");
        ownerRequest.setPage(pageable.getPageNumber());
        ownerRequest.setSize(pageable.getPageSize());

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        PetRequest petRequest = new PetRequest();
        PetDto petDto = new PetDto();
        petDto.setName(null);
        petDto.setColor(null);
        petDto.setBreed(null);
        petRequest.setPetDto(petDto);
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setAction("GET/all");
        petRequest.setPage(0);
        petRequest.setSize(20);

        PetResponse petResponse = waitPetResponse(petRequest);

        if (!petResponse.isSuccess()) {
            return ResponseEntity.status(petResponse.getStatus())
                    .body(null);
        }

        Page<OwnerDto> page = new PageImpl<>(
                resp.getOwnerDtos(),
                PageRequest.of(resp.getPageNumber(), resp.getPageSize()),
                resp.getTotalElements()
        );

        for (OwnerDto ownerDto1 : resp.getOwnerDtos()) {
            ownerDto1.setPetIds(new ArrayList<>());
        }

        for (OwnerDto ownerDto1 : resp.getOwnerDtos()) {
            for (PetDto petDto1 : petResponse.getPetDtos()) {
                if (ownerDto1.getUserId() != null & petDto1.getOwnerId() == ownerDto1.getUserId()) ownerDto1.getPetIds().add(petDto1.getId());
            }
        }

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwner(@PathVariable("id") Long id) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        ownerRequest.setAction("GET/id");
        ownerRequest.setOwnerId(id);

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        PetRequest petRequest = new PetRequest();
        petRequest.setCorrelationId(UUID.randomUUID().toString());
        petRequest.setAction("GET/ownerId");
        petRequest.setOwnerId(resp.getOwnerDto().getUserId());

        PetResponse petResponse = waitPetResponse(petRequest);

        if (!petResponse.isSuccess()) {
            return ResponseEntity.status(petResponse.getStatus())
                    .body(null);
        }

        resp.getOwnerDto().setPetIds(new ArrayList<>());
        for (PetDto petDto : petResponse.getPetDtos()) {
            resp.getOwnerDto().getPetIds().add(petDto.getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp.getOwnerDto());
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllOwners(Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        ownerRequest.setAction("DELETE/all");
        ownerRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwnerById(@PathVariable("id") Long id, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setOwnerId(id);
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        ownerRequest.setAction("DELETE/id");
        ownerRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());
        ownerRequest.setUserId(((MyUserDetails) auth.getPrincipal()).getId());

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<OwnerDto> saveOwner(@RequestBody OwnerDto ownerDto, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        ownerRequest.setOwnerDto(ownerDto);
        ownerRequest.setAction("POST");
        ownerDto.setUserId(((MyUserDetails) auth.getPrincipal()).getId());
        ownerRequest.setOwnerDto(ownerDto);
        ownerRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resp.getOwnerDto());
    }

    @PutMapping
    public ResponseEntity<OwnerDto> updateOwner(@RequestBody OwnerDto ownerDto, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        ownerRequest.setOwnerDto(ownerDto);
        ownerRequest.setAction("PUT");
        ownerDto.setUserId(((MyUserDetails) auth.getPrincipal()).getId());
        ownerRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp.getOwnerDto());
    }

    @PatchMapping
    public ResponseEntity<OwnerDto> modifyOwner(@RequestBody OwnerDto ownerDto, Authentication auth) throws ExecutionException, InterruptedException, TimeoutException {
        OwnerRequest ownerRequest = new OwnerRequest();
        ownerRequest.setCorrelationId(UUID.randomUUID().toString());
        ownerRequest.setOwnerDto(ownerDto);
        ownerRequest.setAction("PATCH");
        ownerDto.setUserId(((MyUserDetails) auth.getPrincipal()).getId());
        ownerRequest.setUserRoles(((MyUserDetails) auth.getPrincipal()).getRolesSet());

        OwnerResponse resp = waitResponse(ownerRequest);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(resp.getStatus())
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resp.getOwnerDto());
    }


    private OwnerResponse waitResponse(OwnerRequest ownerRequest) throws ExecutionException, InterruptedException, TimeoutException {
        String requestTopic = KafkaConfig.OWNER_REQUESTS;
        long replyTimeoutMs = 15_000;
        RequestReplyFuture<String, OwnerRequest, OwnerResponse> future =
                ownerKafka.sendAndReceive(
                        new ProducerRecord<>(requestTopic, null, ownerRequest),
                        Duration.ofMillis(replyTimeoutMs)
                );

        ConsumerRecord<String, OwnerResponse> consumerRecord = future.get(replyTimeoutMs, TimeUnit.MILLISECONDS);
        System.out.println("Received response headers: " + consumerRecord.headers());

        return consumerRecord.value();
    }

    private PetResponse waitPetResponse(PetRequest petRequest) throws ExecutionException, InterruptedException, TimeoutException {
        String requestTopic = KafkaConfig.PET_REQUESTS;
        long replyTimeoutMs = 5_000;
        RequestReplyFuture<String, PetRequest, PetResponse> future =
                petKafka.sendAndReceive(
                        new ProducerRecord<>(requestTopic, null, petRequest),
                        Duration.ofMillis(replyTimeoutMs)
                );

        ConsumerRecord<String, PetResponse> consumerRecord = future.get(replyTimeoutMs, TimeUnit.MILLISECONDS);
        return consumerRecord.value();
    }

}
