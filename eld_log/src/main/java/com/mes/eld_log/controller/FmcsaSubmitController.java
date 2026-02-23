package com.mes.eld_log.controller;

import java.io.File;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mes.eld_log.serviceImpl.FmcsaSmimeEmailService;

import lombok.Data;

@RestController
@RequestMapping("/api/fmcsa")
public class FmcsaSubmitController {

    private final ObjectProvider<FmcsaSmimeEmailService> smimeProvider;

    public FmcsaSubmitController(ObjectProvider<FmcsaSmimeEmailService> smimeProvider) {
        this.smimeProvider = smimeProvider;
    }

    @PostMapping(path = "/email/submit", consumes = "application/json")
    public ResponseEntity<String> submitCsvByEmail(@RequestBody EldSubmissionRequest request) {
        try {
            if (isBlank(request.getFromAddress())) return ResponseEntity.badRequest().body("fromAddress is required");
            if (isBlank(request.getCsvPath())) return ResponseEntity.badRequest().body("csvPath is required");
            if (isBlank(request.getRegistrationId()) || isBlank(request.getIdentifier()))
                return ResponseEntity.badRequest().body("registrationId and identifier are required");

            File csv = new File(request.getCsvPath());
            if (!csv.exists() || !csv.isFile()) {
                return ResponseEntity.badRequest().body("CSV not found at " + request.getCsvPath());
            }

            String subject = (request.isTest() ? "TEST: " : "")
                    + "ELD records from " + request.getRegistrationId().trim()
                    + ":" + request.getIdentifier().trim();

            FmcsaSmimeEmailService smime = smimeProvider.getIfAvailable();
            if (smime == null) {
                return ResponseEntity.status(500).body(
                        "S/MIME service not available. BouncyCastle CMS not visible to Tomcat. " +
                        "Fix: Put bcprov/bcpkix/bcmail/bcutil jars in REAL CATALINA_BASE/lib and restart."
                );
            }

            smime.sendCsvWithSmime(
                    request.getFromAddress().trim(),
                    subject,
                    csv,
                    request.getOutputFileComment()
            );

            return ResponseEntity.ok("ELD CSV submission email sent to FMCSA.");

        } catch (NoClassDefFoundError ncd) {
            return ResponseEntity.status(500).body("BouncyCastle runtime missing: " + ncd.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending email: " + e.getMessage());
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

@Data
class EldSubmissionRequest {
    private boolean test;
    private String fromAddress;
    private String csvPath;
    private String outputFileComment;
    private String registrationId;
    private String identifier;
}
