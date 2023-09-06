package de.iav.backend.repository;

import de.iav.backend.model.Sailor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SailorRepository extends MongoRepository<Sailor, String> {
}
