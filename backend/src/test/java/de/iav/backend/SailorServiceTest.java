package de.iav.backend;
import static org.junit.jupiter.api.Assertions.*;

import de.iav.backend.model.Boat;
import de.iav.backend.model.Sailor;
import de.iav.backend.repository.SailorRepository;
import de.iav.backend.service.SailorService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class SailorServiceTest {

    @InjectMocks
    private SailorService sailorService;

    @Mock
    private SailorRepository sailorRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testUpdateSailorById() {
        String sailorId = "123";
        Sailor existingSailor = new Sailor(
                sailorId, "Paul", "Panzer", "Experte", LocalDate.of(2023, 10, 13),
                new Boat("Pinta", "SV")
        );

        Sailor updatedSailor = new Sailor(
                sailorId, "Updated First Name", "Updated Last Name", "Anfänger", LocalDate.of(2023, 2, 20),
                new Boat("Updated Boat Name", "Updated Boat Type")
        );

        Mockito.when(sailorRepository.findById(sailorId)).thenReturn(Optional.of(existingSailor));
        Mockito.when(sailorRepository.save(updatedSailor)).thenReturn(updatedSailor);

        Sailor result = sailorService.updateSailorById(sailorId, updatedSailor);

        assertNotNull(result);
        assertEquals(updatedSailor, result);

        Mockito.verify(sailorRepository, Mockito.times(1)).findById(sailorId);
        Mockito.verify(sailorRepository, Mockito.times(1)).save(updatedSailor);
    }

    @Test
    void testUpdateSailorByIdSailorNotFound() {
        String sailorId = "123";
        Sailor updatedSailor = new Sailor(
                sailorId, "Updated First Name", "Updated Last Name", "Advanced", LocalDate.of(2023, 2, 20),
                new Boat("Updated Boat Name", "Updated Boat Type")
        );

        Mockito.when(sailorRepository.findById(sailorId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sailorService.updateSailorById(sailorId, updatedSailor));

        Mockito.verify(sailorRepository, Mockito.times(1)).findById(sailorId);
        Mockito.verify(sailorRepository, Mockito.never()).save(updatedSailor);
    }
}