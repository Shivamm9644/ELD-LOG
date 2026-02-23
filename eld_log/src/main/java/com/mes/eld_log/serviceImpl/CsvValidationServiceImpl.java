package com.mes.eld_log.serviceImpl;

import com.mes.eld_log.dtos.ValidateCsvReq;
import com.mes.eld_log.models.ValidationJob;
import com.mes.eld_log.repo.ValidationJobRepo;
import com.mes.eld_log.service.CsvValidationService;
import com.mes.eld_log.service.LlmExplainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvValidationServiceImpl implements CsvValidationService {

    private final ValidationJobRepo jobRepo;
    private final LlmExplainService llmExplainService;

    @Value("${eld.workDir}") private String workDir;

    @Override
    public String startValidation(ValidateCsvReq req) {
        String requestId = UUID.randomUUID().toString();

        ValidationJob job = ValidationJob.builder()
            .requestId(requestId)
            .status("PENDING")
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        jobRepo.save(job);
        runAsync(job, req);
        return requestId;
    }

    @Async
    void runAsync(ValidationJob job, ValidateCsvReq req) {
        try {
            job.setStatus("PASS"); // assume pass for now
            job.setHumanReply(
                llmExplainService.makeHumanReply("PASS", 0, null, job.getRequestId())
            );
        } catch (Exception e) {
            job.setStatus("ERROR");
        } finally {
            job.setUpdatedAt(Instant.now());
            jobRepo.save(job);
        }
    }
}
