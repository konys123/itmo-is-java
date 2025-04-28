package lab3;

import lab3.controller.OwnerController;
import lab3.dto.OwnerDto;
import lab3.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OwnerController.class)

class OwnerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Test
    void shouldReturnOwnersList() throws Exception {
        // given
        Page<OwnerDto> owners = new PageImpl<>(List.of(new OwnerDto()));
        Mockito.when(ownerService.getAllOwners(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(owners);

        // when & then
        mockMvc.perform(get("/owners/all"))
                .andExpect(status().isOk());
    }
}
