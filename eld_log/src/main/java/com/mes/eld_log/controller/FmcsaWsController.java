package com.mes.eld_log.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fmcsa.eldws.DiagnosticResponse;
import com.fmcsa.eldws.ELDSubmissionResponse;
import com.mes.eld_log.serviceImpl.FmcsaEldService;




@RestController
@RequestMapping("/api/fmcsa/ws")
public class FmcsaWsController {

  private final FmcsaEldService fmcsa;

  public FmcsaWsController(FmcsaEldService fmcsa) {
    this.fmcsa = fmcsa;
  }

  @GetMapping("/ping")
  public ResponseEntity<?> ping(@RequestParam String eldIdentifier,
                                @RequestParam String eldRegistrationId) {
    DiagnosticResponse resp = fmcsa.ping(eldIdentifier, eldRegistrationId);
    return ResponseEntity.ok(resp);
  }

  @PostMapping("/submit")
  public ResponseEntity<?> submit(@RequestBody SubmitReq req) {
    ELDSubmissionResponse resp = fmcsa.submitCsv(
        req.eldIdentifier,
        req.eldRegistrationId,
        req.outputFilename,
        req.outputFileBody,
        req.outputFileComment,
        req.test
    );
    return ResponseEntity.ok(resp);
  }

  public static class SubmitReq {
    public String eldIdentifier;
    public String eldRegistrationId;
    public String outputFilename;
    public String outputFileBody;
    public String outputFileComment;
    public boolean test;
  }
}
