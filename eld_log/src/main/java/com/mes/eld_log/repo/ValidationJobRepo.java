package com.mes.eld_log.repo;

import com.mes.eld_log.models.ValidationJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ValidationJobRepo extends MongoRepository<ValidationJob, String> {
    Optional<ValidationJob> findByRequestId(String requestId);
}
