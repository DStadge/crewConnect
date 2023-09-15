package de.iav.frontend.model;

import java.time.LocalDate;

public record SailorWithoutId (
    String firstname,
    String lastname,
    String experience,

    //LocalDate in JSON "YYYY-MM-DD"
    LocalDate sailDate
) {
}
