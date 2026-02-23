package com.mes.eld_log.service;

import java.util.List;

public interface GeminiAiService {

    String explain(
        String status,
        Integer errorCount,
        List<String> errors,
        List<String> mappedEvents,
        String requestId
    );
}
