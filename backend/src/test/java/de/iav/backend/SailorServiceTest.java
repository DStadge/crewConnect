package de.iav.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.iav.backend.model.Sailor;
import de.iav.backend.repository.SailorRepository;
import de.iav.backend.service.SailorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class SailorServiceTest {

    @InjectMocks
    private SailorService sailorService;

    @Mock
    private SailorRepository sailorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sailorRepository = mock(SailorRepository.class);
        sailorService = new SailorService(sailorRepository);
    }

    @Test
    void testUpdateSailorByIdSailorFound() {
        String sailorId = "123";
        Sailor existingSailor = new Sailor(
                sailorId, "Paul", "Panzer", "Experte", LocalDate.of(2023, 10, 13));

        Sailor updatedSailor = new Sailor(
                sailorId, "NeuerVorName", "NeuerNachName", "Anfänger", LocalDate.of(2023, 2, 20));

        // Mocken des Repository-Verhaltens
        when(sailorRepository.findById(sailorId)).thenReturn(Optional.of(existingSailor));
        when(sailorRepository.save(updatedSailor)).thenReturn(updatedSailor);

        // Die Methode aufrufen und das Ergebnis überprüfen
        Sailor result = sailorService.updateSailorById(sailorId, updatedSailor);

        assertNotNull(result);
        assertEquals(updatedSailor, result);

        // Überprüfen, ob die Repository-Methoden korrekt aufgerufen wurden
        verify(sailorRepository, times(1)).findById(sailorId);
        verify(sailorRepository, times(1)).save(updatedSailor);
    }

    @Test
    void testListAllSailor() {
        Sailor sailor1 = new Sailor("1", "Paul", "Panzer", "Anfänger", LocalDate.of(2023, 12, 13));
        Sailor sailor2 = new Sailor("2", "Paule", "Panzer", "Experte", LocalDate.of(2023, 2, 14));
        List<Sailor> sailorList = new ArrayList<>();
        sailorList.add(sailor1);
        sailorList.add(sailor2);

        when(sailorRepository.findAll()).thenReturn(sailorList);

        List<Sailor> result = sailorService.listAllSailor();

        assertEquals(2, result.size());
        assertEquals(sailor1, result.get(0));
        assertEquals(sailor2, result.get(1));
    }

    @Test
    void testListAllSailorEmpty() {
        when(sailorRepository.findAll()).thenReturn(new ArrayList<>());

        List<Sailor> result = sailorService.listAllSailor();

        assertEquals(0, result.size());
    }

    @Test
    void testAddSailor() {
        Sailor sailorToAdd = new Sailor("1", "Paul", "Panzer", "Anfänger", LocalDate.of(2023, 11, 16));

        when(sailorRepository.save(sailorToAdd)).thenReturn(sailorToAdd);

        Sailor result = sailorService.addSailor(sailorToAdd);

        assertEquals(sailorToAdd, result);
    }

    @Test
    void testGetSailorById() {
        String sailorId = "1";
        Sailor expectedSailor = new Sailor(sailorId, "Paul", "Panzer", "Anfänger", LocalDate.of(2023, 4, 11));

        when(sailorRepository.findById(sailorId)).thenReturn(Optional.of(expectedSailor));

        Optional<Sailor> result = sailorService.getSailorById(sailorId);

        assertTrue(result.isPresent());

        assertEquals(expectedSailor, result.get());
    }

    @Test
    void testGetSailorByIdSailorNotFound() {
        String sailorId = "2";

        when(sailorRepository.findById(sailorId)).thenReturn(Optional.empty());

        Optional<Sailor> result = sailorService.getSailorById(sailorId);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteSailorById() {
        String sailorId = "1";

        sailorService.deleteSailorById(sailorId);

        verify(sailorRepository, times(1)).deleteById(sailorId);
    }

    @Test
    void testUpdateSailorById() {
        String sailorId = "1";
        Sailor existingSailor = new Sailor(sailorId, "Vorname", "Nachname", "Erfahren", LocalDate.now());
        Sailor updatedSailor = new Sailor(sailorId, "NeuerVorname", "NeuerNachname", "Anfänger", LocalDate.now());

        when(sailorRepository.findById(sailorId)).thenReturn(Optional.of(existingSailor));
        when(sailorRepository.save(updatedSailor)).thenReturn(updatedSailor);

        Sailor result = sailorService.updateSailorById(sailorId, updatedSailor);

        assertNotNull(result);
        assertEquals(updatedSailor, result);

        verify(sailorRepository, times(1)).findById(sailorId);
        verify(sailorRepository, times(1)).save(updatedSailor);
    }

    @Test
    void testUpdateSailorByIdSailorNotFound() {
        String sailorId = "1";
        Sailor updatedSailor = new Sailor(sailorId, "NeuerVorname", "NeuerNachname", "Anfänger", LocalDate.now());

        when(sailorRepository.findById(sailorId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> sailorService.updateSailorById(sailorId, updatedSailor));

        verify(sailorRepository, times(1)).findById(sailorId);
        verify(sailorRepository, never()).save(updatedSailor);
    }
}