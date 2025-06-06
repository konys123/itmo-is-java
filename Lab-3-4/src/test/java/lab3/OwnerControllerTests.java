package lab3;

import jakarta.persistence.EntityNotFoundException;
import lab3.controller.OwnerController;
import lab3.dto.OwnerDto;
import lab3.security.CustomSecurityConfiguration;
import lab3.services.OwnerDetailsService;
import lab3.services.OwnerService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CustomSecurityConfiguration.class)
@WebMvcTest(OwnerController.class)
class OwnerControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private OwnerService ownerService;

    @MockitoBean
    private OwnerDetailsService ownerDetailsService;

    @Test
    void getOwnerById() throws Exception {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setId(8L);
        ownerDto.setName("LOL");

        Mockito.when(this.ownerService.getOwnerById(8L)).thenReturn(ownerDto);
        mvc.perform(get("/owners/8").with(user("LOL").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8L))
                .andExpect(jsonPath("$.name").value("LOL"));
    }

    @Test
    void getAllOwners() throws Exception {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setId(8L);
        ownerDto.setName("LOL");

        OwnerDto ownerDto2 = new OwnerDto();
        ownerDto2.setId(9L);
        ownerDto2.setName("KEK");

        List<OwnerDto> ownerDtos = List.of(ownerDto, ownerDto2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<OwnerDto> page = new PageImpl<>(ownerDtos, pageable, ownerDtos.size());

        Mockito.when(this.ownerService.getAllOwners(any(), any(), any())).thenReturn(page);

        mvc.perform(get("/owners/all").with(user("LOL").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(8L))
                .andExpect(jsonPath("$.content[0].name").value("LOL"))
                .andExpect(jsonPath("$.content[1].id").value(9L))
                .andExpect(jsonPath("$.content[1].name").value("KEK"));
    }

    @Test
    void getOwnerByIdReturnNotFoundStatus() throws Exception {
        Mockito.when(this.ownerService.getOwnerById(99L))
                .thenThrow(new EntityNotFoundException("Owner не найден с id=99"));

        mvc.perform(get("/owners/99").with(user("LOL").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void NotFoundStatus() throws Exception {
        mvc.perform(get("/users").with(user("LOL").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOwnerByIdReturnUnauthorizedStatus() throws Exception {
        mvc.perform(get("/owners/8").with(anonymous()))
                .andExpect(status().isUnauthorized());

    }
}