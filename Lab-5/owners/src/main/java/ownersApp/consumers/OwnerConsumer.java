package ownersApp.consumers;

import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.SendTo;
import ownersApp.Exceptions.ForbiddenException;

import ownersApp.config.KafkaConfig;
import ownersApp.dto.OwnerDto;
import ownersApp.dto.OwnerRequest;
import ownersApp.dto.OwnerResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ownersApp.services.OwnerService;


@Component
@RequiredArgsConstructor
public class OwnerConsumer {
    private final OwnerService ownerService;

    @KafkaListener(topics = KafkaConfig.OWNER_REQUESTS,
            groupId = "owner-group",
            containerFactory = "ownerKafkaListenerContainerFactory")
    @SendTo
    public OwnerResponse handle(ConsumerRecord<String, OwnerRequest> record) {
        OwnerRequest req = record.value();
        OwnerResponse resp = new OwnerResponse();
        resp.setCorrelationId(req.getCorrelationId());
        try {
            switch (req.getAction()) {
                case "GET/all" -> {
                    Page<OwnerDto> owners = ownerService.getAllOwners(
                            req.getOwnerDto().getName(),
                            req.getOwnerDto().getBirthDate(),
                            PageRequest.of(req.getPage(), req.getSize())
                    );
                    resp.setSuccess(true);
                    resp.setStatus(200);
                    resp.setOwnerDtos(owners.getContent());
                    resp.setTotalElements(owners.getTotalElements());
                    resp.setTotalPages(owners.getTotalPages());
                    resp.setPageNumber(owners.getNumber());
                    resp.setPageSize(owners.getSize());
                }
                case "GET/id" -> {
                    OwnerDto OwnerDto = ownerService.getOwnerById(req.getOwnerId());
                    resp.setSuccess(true);
                    resp.setStatus(200);
                    resp.setOwnerDto(OwnerDto);
                }
                case "DELETE/all" -> {
                    ownerService.deleteAllOwners(req.getUserRoles());
                    resp.setSuccess(true);
                    resp.setStatus(204);
                }
                case "DELETE/id" -> {
                    ownerService.deleteOwnerById(req.getOwnerId(), req.getUserId(), req.getUserRoles());
                    resp.setSuccess(true);
                    resp.setStatus(204);
                }
                case "POST" -> {
                    OwnerDto OwnerDto = ownerService.saveOwner(req.getOwnerDto());
                    resp.setOwnerDto(OwnerDto);
                    resp.setSuccess(true);
                    resp.setStatus(201);
                }
                case "PUT" -> {
                    OwnerDto OwnerDto = ownerService.updateOwner(req.getOwnerDto(), req.getUserRoles());
                    resp.setOwnerDto(OwnerDto);
                    resp.setSuccess(true);
                    resp.setStatus(200);
                }
                case "PATCH" -> {
                    OwnerDto OwnerDto = ownerService.modifyOwner(req.getOwnerDto(), req.getUserRoles());
                    resp.setOwnerDto(OwnerDto);
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
