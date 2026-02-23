package com.mes.eld_log.controller;

import com.mes.eld_log.dtos.*;
import com.mes.eld_log.repo.ValidationJobRepo;
import com.mes.eld_log.service.CsvValidationService;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eld")
public class EldCsvValidationController {

    private final CsvValidationService service;
    private final ValidationJobRepo repo;

    public EldCsvValidationController(CsvValidationService service, ValidationJobRepo repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/validate")
    public ValidateCsvStartRes validate(@Valid @RequestBody ValidateCsvReq req) {
        String id = service.startValidation(req);
        return new ValidateCsvStartRes(id, "PENDING", "Validation started");
    }

    @GetMapping("/status/{id}/reply")
    public HumanReplyRes reply(@PathVariable String id) {
        return repo.findByRequestId(id)
            .map(j -> new HumanReplyRes(j.getHumanReply()))
            .orElse(new HumanReplyRes("here are some mistakes"));
    }
}
