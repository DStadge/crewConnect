package de.iav.backend.repository;

import de.iav.backend.model.Sailor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SailorRepository extends MongoRepository<Sailor, String> {
}
