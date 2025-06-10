package httpWebGateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class OwnerResponse {
    private String correlationId;
    private boolean success;
    private OwnerDto ownerDto;
    private String errorMessage;
    private int status;
    private List<OwnerDto> ownerDtos;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}
