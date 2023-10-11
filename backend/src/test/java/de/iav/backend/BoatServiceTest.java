package de.iav.backend;

import de.iav.backend.model.Boat;
import de.iav.backend.repository.BoatRepository;
import de.iav.backend.service.BoatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BoatServiceTest {
/*
    @InjectMocks
    private BoatService boatService;

    @Mock
    private BoatRepository boatRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testUpdateBoatById_ExistingBoat() {
        String boatId = "boat123"; // Überprüfe, ob diese ID im Mock verwendet wird
        Boat existingBoat = new Boat(boatId, "Type1"); // Überprüfe die ID in den Mock-Daten
        Boat updatedBoat = new Boat("UpdatedBoat", "UpdatedType");

        // Konfiguriere das Mock-Verhalten
        when(boatRepository.findById(boatId)).thenReturn(Optional.of(existingBoat));
        when(boatRepository.save(updatedBoat)).thenReturn(updatedBoat);

        // When
        Boat result = boatService.updateBoatById(boatId, updatedBoat);

        // Then
        assertEquals(updatedBoat, result);
        verify(boatRepository, times(1)).findById(boatId);
        verify(boatRepository, times(1)).save(updatedBoat);
    }

    @Test
    void testUpdateBoatById_NonExistingBoat() {
        // Given
        String boatId = "12";
        Boat updatedBoat = new Boat("UpdatedBoat", "UpdatedType");
        when(boatRepository.findById(boatId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(NoSuchElementException.class, () -> boatService.updateBoatById(boatId, updatedBoat));
        verify(boatRepository, times(1)).findById(boatId);
        verify(boatRepository, never()).save(any());
    }

    @Test
    void testListAllBoats() {
        Boat boat1 = new Boat("Pinta", "SV");
        Boat boat2 = new Boat("Chrysaor", "MV");
        List<Boat> boatList = new ArrayList<>();
        boatList.add(boat1);
        boatList.add(boat2);

        // Wenn die Methode findAll() im Repository aufgerufen wird, gib die Beispieldaten zurück
        when(boatRepository.findAll()).thenReturn(boatList);

        // Führe die Methode listAllBoats() im Service aus
        List<Boat> result = boatService.listAllBoats();

        // Überprüfen Sie die Ergebnisse
        assertEquals(2, result.size());
        assertEquals("Pinta", result.get(0).boatName());
        assertEquals("SV", result.get(0).boatType());
        assertEquals("Chrysaor", result.get(1).boatName());
        assertEquals("MV", result.get(1).boatType());
    }*/
}