package de.iav.frontend.model;

import java.time.LocalDate;

public record SailorWithoutId (
    String firstName,
    String lastName,
    String experience,

    //LocalDate in JSON "YYYY-MM-DD"
    LocalDate sailDate
) {
}
