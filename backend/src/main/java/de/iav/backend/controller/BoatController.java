package de.iav.backend.controller;
import de.iav.backend.model.Boat;
import de.iav.backend.service.BoatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crewconnect/boats")
public class BoatController {

    private final BoatService boatService;

    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    @GetMapping
    public List<Boat> listAllBoats() {
        return boatService.listAllBoats();
    }

    @PostMapping
    public Boat addBoat(@RequestBody Boat boatToAdd) {
        return boatService.addBoat(boatToAdd);
    }

    @GetMapping("/{id}")
    public Optional<Boat> getBoatById(@PathVariable String id) {
        return boatService.getBoatById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBoatById(@PathVariable String id) {
        boatService.deleteBoatById(id);
    }

    @PutMapping("/{id}")
    public Boat updateBoatById(@PathVariable String id, @RequestBody Boat boatToUpdate) {
        return boatService.updateBoatById(id, boatToUpdate);
    }
}