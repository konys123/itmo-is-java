package petsApp.consumers;

import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.SendTo;
import petsApp.config.KafkaConfig;
import petsApp.Exceptions.ForbiddenException;
import petsApp.dto.PetDto;
import petsApp.dto.PetRequest;
import petsApp.dto.PetResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import petsApp.services.PetService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PetConsumer {
    private final PetService petService;

    @KafkaListener(topics = KafkaConfig.PET_REQUESTS,
            groupId = "pet-group",
            containerFactory = "petKafkaListenerContainerFactory")
    @SendTo
    public PetResponse handle(ConsumerRecord<String, PetRequest> record) {
        PetRequest req = record.value();
        PetResponse resp = new PetResponse();
        resp.setCorrelationId(req.getCorrelationId());
        try {
            switch (req.getAction()) {
                case "GET/all" -> {
                    Page<PetDto> pets = petService.getAllPets(
                            req.getPetDto().getColor(),
                            req.getPetDto().getName(),
                            req.getPetDto().getBreed(),
                            PageRequest.of(req.getPage(), req.getSize())
                    );
                    resp.setSuccess(true);
                    resp.setStatus(200);
                    resp.setPetDtos(pets.getContent());
                    resp.setTotalElements(pets.getTotalElements());
                    resp.setTotalPages(pets.getTotalPages());
                    resp.setPageNumber(pets.getNumber());
                    resp.setPageSize(pets.getSize());

                }
                case "GET/id" -> {
                    PetDto petDto = petService.getPetById(req.getPetId());
                    resp.setSuccess(true);
                    resp.setStatus(200);
                    resp.setPetDto(petDto);
                }
                case "DELETE/all" -> {
                    petService.deleteAllPets(req.getUserRoles());
                    resp.setSuccess(true);
                    resp.setStatus(204);
                }
                case "DELETE/id" -> {
                    petService.deletePetById(req.getPetId(), req.getOwnerId(), req.getUserRoles());
                    resp.setSuccess(true);
                    resp.setStatus(204);
                }
                case "POST" -> {
                    req.getPetDto().setOwnerId(req.getOwnerId());
                    PetDto petDto = petService.savePet(req.getPetDto());
                    resp.setPetDto(petDto);
                    resp.setSuccess(true);
                    resp.setStatus(200);
                }
                case "PUT" -> {
                    req.getPetDto().setOwnerId(req.getOwnerId());
                    PetDto petDto = petService.updatePet(req.getPetDto(), req.getOwnerId(), req.getUserRoles());
                    resp.setPetDto(petDto);
                    resp.setSuccess(true);
                    resp.setStatus(200);
                }
                case "PATCH" -> {
                    req.getPetDto().setOwnerId(req.getOwnerId());
                    PetDto petDto = petService.modifyPet(req.getPetDto(), req.getOwnerId(), req.getUserRoles());
                    resp.setPetDto(petDto);
                    resp.setSuccess(true);
                    resp.setStatus(200);
                }
                case "GET/ownerId" -> {
                    List<PetDto> petDtos = petService.getPetByOwnerId(req.getOwnerId());
                    resp.setPetDtos(petDtos);
                    resp.setSuccess(true);
                    resp.setStatus(200);
                }
            }

        } catch (ForbiddenException ex) {
            resp.setSuccess(false);
            resp.setStatus(403);
            resp.setErrorMessage(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            resp.setSuccess(false);
            resp.setStatus(404);
            resp.setErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            resp.setSuccess(false);
            resp.setStatus(500);
            resp.setErrorMessage(ex.getMessage());
        }

        return resp;
    }
}
