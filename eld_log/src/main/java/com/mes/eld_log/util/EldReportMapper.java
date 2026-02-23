package com.mes.eld_log.util;

import com.mes.eld_log.dtos.EldReportDto;
import com.mes.eld_log.models.ValidationJob;

public class EldReportMapper {

    public static void map(EldReportDto dto, ValidationJob job) {

        if (dto.getEldHeader() != null) {
            job.setEldIdentifier(dto.getEldHeader().getEldIdentifier());
            job.setEldProvider(dto.getEldHeader().getEldProvider());
            job.setEldSoftwareVersion(dto.getEldHeader().getEldSoftwareVersion());
            job.setFileGeneratedTime(dto.getEldHeader().getFileGeneratedTime());
        }

        if (dto.getCarrier() != null) {
            job.setCarrierName(dto.getCarrier().getCarrierName());
            job.setUsdotNumber(dto.getCarrier().getUsdotNumber());
        }

        if (dto.getDriver() != null) {
            job.setDriverId(dto.getDriver().getDriverId());
            job.setDriverName(dto.getDriver().getDriverName());
        }

        if (dto.getVehicle() != null) {
            job.setVehicleUnitNumber(dto.getVehicle().getUnitNumber());
            job.setVehicleVin(dto.getVehicle().getVin());
        }

        if (dto.getEventRecords() != null) {
            job.setEventCount(dto.getEventRecords().size());
        }
    }
}
