package de.iav.frontend.model;

import java.time.LocalDate;

public record Sailor(
        String sailorId,
        String firstname,
        String lastname,

        String experience,
        //LocalDate in JSON "YYYY-MM-DD"
        LocalDate sailDate
) {
    public String getFirstName() {
        return this.firstname;
    }

    public String getLastName() {
        return this.lastname;
    }

    public String getExperience() {
        return this.experience;
    }

    public LocalDate getSailDate() {
        return this.sailDate;
    }


}
