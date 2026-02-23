package com.mes.eld_log.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateCsvStartRes {
    private String requestId;
    private String status;
    private String message;
}
