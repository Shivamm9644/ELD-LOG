package com.mes.eld_log.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventLineIndex {
    private String eventSeqId;
    private String eventTime;
    private String eventType;
    private int csvLineNumber;
}
