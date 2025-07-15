package petsApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class PetResponse {
    private String correlationId;
    private boolean success;
    private PetDto petDto;
    private String errorMessage;
    private int status;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private List<PetDto> petDtos;
}
