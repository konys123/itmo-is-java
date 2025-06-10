package lab3;

import jakarta.persistence.EntityNotFoundException;
import lab3.controller.PetController;
import lab3.dto.PetDto;
import lab3.entities.Colors;

import lab3.services.OwnerDetailsService;
import lab3.services.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private OwnerDetailsService ownerDetailsService;

    @Test
    void getPetById() throws Exception {
        PetDto petDto = new PetDto();
        petDto.setId(1L);
        petDto.setName("Barsik");
        petDto.setBreed("xz");
        petDto.setOwnerId(1L);
        petDto.setColor(Colors.GOLDEN);
        petDto.setBirthDate(LocalDate.parse("2025-04-30"));

        Mockito.when(this.petService.getPetById(1L)).thenReturn(petDto);
        mvc.perform(get("/pets/1").with(user("LOL").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Barsik"))
                .andExpect(jsonPath("$.breed").value("xz"))
                .andExpect(jsonPath("$.ownerId").value(1L))
                .andExpect(jsonPath("$.color").value("GOLDEN"))
                .andExpect(jsonPath("$.birthDate").value("2025-04-30"));
    }

    @Test
    void getAllOPets() throws Exception {
        PetDto PetDto = new PetDto();
        PetDto.setId(1L);
        PetDto.setName("Barsik");

        PetDto PetDto2 = new PetDto();
        PetDto2.setId(2L);
        PetDto2.setName("Persik");

        List<PetDto> PetDtos = List.of(PetDto, PetDto2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<PetDto> page = new PageImpl<>(PetDtos, pageable, PetDtos.size());

        Mockito.when(this.petService.getAllPets(any(), any(), any(), any())).thenReturn(page);

        mvc.perform(get("/pets/all").with(user("LOL").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Barsik"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Persik"));
    }

    @Test
    void getPetByIdReturnNotFoundStatus() throws Exception {
        Mockito.when(this.petService.getPetById(99L))
                .thenThrow(new EntityNotFoundException("Owner не найден с id=99"));

        mvc.perform(get("/pets/99").with(user("LOL").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void notFoundStatus() throws Exception {
        mvc.perform(get("/petss/pet").with(user("LOL").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void unauthorizedStatus() throws Exception {
        mvc.perform(get("/pets/all").with(anonymous()))
                .andExpect(status().isUnauthorized());

    }
}