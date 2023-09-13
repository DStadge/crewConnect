package de.iav.backend.controller;

import de.iav.backend.model.Sailor;
import de.iav.backend.service.SailorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SailorController {


    private final SailorService sailorService;


    public SailorController(SailorService sailorService) {
        this.sailorService = sailorService;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Testnachricht ohne Security";
    }

    @GetMapping("/crewconnect/sailor")
    public List<Sailor> listAllSailor(){
        return sailorService.listAllSailor();
    }

    @PostMapping("/crewconnect/sailor")
    public Sailor addSailor(@RequestBody Sailor sailorToAdd){
        return sailorService.addSailor(sailorToAdd);
    }

    @GetMapping("/crewconnect/{id}")
    public Optional<Sailor> getSailorById(@PathVariable String id){
        return sailorService.getSailorById(id);
    }
    @DeleteMapping("/crewconnect/{id}")
    public void deleteSailorById(@PathVariable String id){
        sailorService.deleteSailorById(id);
    }

    @PutMapping("/crewconnect/{id}")
    public Sailor updateSailorById(@PathVariable String id, @RequestBody Sailor sailorToUpdate){
        return sailorService.updateSailorById(id, sailorToUpdate);
    }
}

