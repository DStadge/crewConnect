package de.iav.backend.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public record Sailor(

        @Id
        String sailorId,
        String firstName,
        String lastName,
        String experience,
        LocalDate sailDate
) {
}