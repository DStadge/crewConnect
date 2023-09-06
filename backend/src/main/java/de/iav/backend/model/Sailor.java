package de.iav.backend.model;

import java.time.LocalDate;

public record Sailor(
        String id,
        String firstName,
        String lastName,
        String age,
        String experience,
        LocalDate sailDate
) {
}
