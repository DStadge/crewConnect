package de.iav.frontend.model;

import java.time.LocalDate;

public record Sailor(

        String sailorId,
        String firstName,
        String lastName,
        String experience,
        LocalDate sailDate
) {
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getExperience() {
        return this.experience;
    }

    public LocalDate getSailDate() {
        return this.sailDate;
    }
}