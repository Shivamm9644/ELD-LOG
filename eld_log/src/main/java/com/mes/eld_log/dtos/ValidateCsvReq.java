package com.mes.eld_log.dtos;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ValidateCsvReq {
    @NotNull private Integer driverId;
    @NotBlank private String fromDate;
    @NotBlank private String toDate;
    @NotBlank private String email;
}
