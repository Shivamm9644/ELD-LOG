package com.mes.eld_log.service;

import com.mes.eld_log.dtos.ValidateCsvReq;

public interface CsvValidationService {
    String startValidation(ValidateCsvReq req);
}
