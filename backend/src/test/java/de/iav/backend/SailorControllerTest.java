package de.iav.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.model.Sailor;
import de.iav.backend.service.SailorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class SailorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Autowired
    private SailorService sailorService;

    @Test
    void testAddSailor() throws Exception {
        // Erstelle ein gültiges Boat-Objekt
        Boat boat = new Boat("BoatName", "BoatType");

        Sailor sailorToAdd = new Sailor(
                "3",
                "Paul",
                "Panzer",
                "experte",
                LocalDate.of(2023, 9, 22),
                boat
        );

        // Wenn sailorService.addSailor aufgerufen wird, geben wir sailorToAdd zurück.
        when(sailorService.addSailor(sailorToAdd)).thenReturn(sailorToAdd);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/crewconnect/sailor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sailorToAdd)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sailorToAdd)));
    }


    @Test
    void getAllSailor() throws Exception {
        List<Sailor> sailorList = new ArrayList<>();
        Sailor sailor1 = new Sailor("1", "Paul",
                "Panzer",
                "gut",
                LocalDate.of(2022, 1, 1),
                new Boat("Pinta", "SV"));
        Sailor sailor2 = new Sailor("2",
                "Paulina",
                "Panzer2",
                "experte",
                LocalDate.of(2023,
                        2,
                        15),
                new Boat("Pinta", "SV"));
        sailorList.add(sailor1);
        sailorList.add(sailor2);

        // Definieren Sie das erwartete Verhalten Ihres Mocked SailorService
        when(sailorService.listAllSailor()).thenReturn(sailorList);

        // Führen Sie den Controller-Test durch
        mockMvc.perform(MockMvcRequestBuilders.get("/api/crewconnect/sailor"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sailorList)));
    }

    @Test
    void getSailorById() throws Exception {
        // Erstellen Sie einen Dummy-Sailor mit einer bestimmten ID, den Ihr Mocked SailorService zurückgeben wird
        Sailor expectedSailor = new Sailor("1",
                "Max",
                "Mustermann",
                "Erfahren",
                LocalDate.of(2022, 1, 1),
                new Boat("Pinta", "SV"));

        // Definieren Sie das erwartete Verhalten Ihres Mocked SailorService
        when(sailorService.getSailorById("1")).thenReturn(Optional.of(expectedSailor));

        // Führen Sie den Controller-Test durch, um einen Seemann nach ID abzurufen
        mockMvc.perform(MockMvcRequestBuilders.get("/api/crewconnect/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedSailor)));
    }

    @Test
    void deleteSailorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/crewconnect/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateSailorById() throws Exception {
        Sailor updatedSailor = new Sailor("1",
                "NeuerVorname",
                "NeuerNachname",
                "NeueErfahrung", LocalDate.of(2023, 9, 22),
        new Boat("Pinta", "SV"));

        when(sailorService.updateSailorById("1", updatedSailor)).thenReturn(updatedSailor);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/crewconnect/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSailor)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updatedSailor)));
    }
}