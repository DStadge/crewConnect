package de.iav.backend.service;

import de.iav.backend.model.Sailor;
import de.iav.backend.repository.SailorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class SailorService {

    private final SailorRepository sailorRepository;

    public SailorService(SailorRepository sailorRepository) {
        this.sailorRepository = sailorRepository;
    }

    public List<Sailor> listAllSailor(){
        return sailorRepository.findAll();
    }

    public Sailor addSailor(Sailor sailorToAdd){
        return sailorRepository.save(sailorToAdd);
    }

    public Optional<Sailor> getSailorById(String sailorId){
        return sailorRepository.findById(sailorId);
    }
    public void deleteSailorById(String sailorId){
        sailorRepository.deleteById(sailorId);
    }
    public Sailor updateSailorById(String id, Sailor sailorToUpdate){
        Optional<Sailor> existingSailor = sailorRepository.findById(id);
        if (existingSailor.isPresent()) {
            return sailorRepository.save(sailorToUpdate);
        }
        throw new NoSuchElementException("Sailor with ID: " + id + " not found!");

    }
}
