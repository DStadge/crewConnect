package de.iav.backend.controller;

import de.iav.backend.model.Sailor;
import de.iav.backend.service.SailorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crewconnect")
public class SailorController {


    private final SailorService sailorService;


    public SailorController(SailorService sailorService) {
        this.sailorService = sailorService;
    }

 /*   @GetMapping("/hello")
    public String hello(){
        return "Hier bin ich im Internet zu finden!";
    }
*/
    @GetMapping("/sailor")
    public List<Sailor> listAllSailor(){
        return sailorService.listAllSailor();
    }

    @PostMapping("/sailor")
    public Sailor addSailor(@RequestBody Sailor sailorToAdd){
        return sailorService.addSailor(sailorToAdd);
    }

    @GetMapping("/{id}")
    public Optional<Sailor> getSailorById(@PathVariable String id){
        return sailorService.getSailorById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteSailorById(@PathVariable String id){
        sailorService.deleteSailorById(id);
    }

    @PutMapping("/{id}")
    public Sailor updateSailorById(@PathVariable String id, @RequestBody Sailor sailorToUpdate){
        return sailorService.updateSailorById(id, sailorToUpdate);
    }
}

