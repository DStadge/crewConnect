package de.iav.backend.service;

import de.iav.backend.model.Sailor;
import de.iav.backend.repository.SailorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SailorService {

    private static final Logger logger = LoggerFactory.getLogger(SailorService.class);

    private final SailorRepository sailorRepository;

    public SailorService(SailorRepository sailorRepository) {
        this.sailorRepository = sailorRepository;
    }

    public List<Sailor> listAllSailor(){
      //  logger.info("List all sailors");
      //  logger.debug("List all sailors");
        return sailorRepository.findAll();
    }

    public Sailor addSailor(Sailor sailorToAdd){

      //  logger.info("Add sailor");
        return sailorRepository.save(sailorToAdd);
    }

    public Optional<Sailor> getSailorById(String sailorId){

      //  logger.info("Get sailor by id");
        return sailorRepository.findById(sailorId);
    }
    public void deleteSailorById(String sailorId){

      //  logger.info("Delete sailor by id");
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
