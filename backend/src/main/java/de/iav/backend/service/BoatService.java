package de.iav.backend.service;
import de.iav.backend.model.Boat;
import de.iav.backend.repository.BoatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BoatService {

    private final BoatRepository boatRepository;

    public BoatService(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    public List<Boat> listAllBoats() {
        return boatRepository.findAll();
    }

    public Boat addBoat(Boat boatToAdd) {
        return boatRepository.save(boatToAdd);
    }

    public Optional<Boat> getBoatById(String boatId) {
        return boatRepository.findById(boatId);
    }

    public void deleteBoatById(String boatId) {
        boatRepository.deleteById(boatId);
    }

    public Boat updateBoatById(String id, Boat boatToUpdate) {
        Optional<Boat> existingBoat = boatRepository.findById(id);
        if (existingBoat.isPresent()) {
            return boatRepository.save(boatToUpdate);
        }
        throw new NoSuchElementException("Boot mit der ID: " + id + " nicht gefunden");
    }
}
