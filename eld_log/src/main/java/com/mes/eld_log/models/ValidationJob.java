package com.mes.eld_log.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("validation_jobs")
public class ValidationJob {

    @Id
    private String id;

    // --- Job meta ---
    private String requestId;
    private String status; // PENDING, RUNNING, PASS, FAIL, ERROR

    // --- ELD Header ---
    private String eldIdentifier;
    private String eldProvider;
    private String eldSoftwareVersion;
    private String fileGeneratedTime;

    // --- Carrier ---
    private String carrierName;
    private String usdotNumber;

    // --- Driver ---
    private String driverId;
    private String driverName;

    // --- Vehicle ---
    private String vehicleUnitNumber;
    private String vehicleVin;

    // --- Summary ---
    private Integer eventCount;

    // --- Files ---
    private String csvPath;
    private String reportPath;

    // --- Validator result ---
    private Integer errorCount;
    private List<String> errors;

    // --- Gemini / human reply ---
    private String humanReply;

    // --- Debug (optional, truncated) ---
    private String validatorRaw;

    // --- Audit ---
    private Instant createdAt;
    private Instant updatedAt;
 // --- Event ↔ CSV mapping ---
    private List EldReportDto;

    // --- FMCSA submit ---
    private String fmcsaSubmissionId;
    private String fmcsaSubmitStatus;   // SUBMITTED / FAILED
    private String fmcsaSubmitMessage;

}
