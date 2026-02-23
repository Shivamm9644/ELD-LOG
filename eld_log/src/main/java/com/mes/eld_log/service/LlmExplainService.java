package com.mes.eld_log.service;

import java.util.List;

public interface LlmExplainService {
    String makeHumanReply(String status, Integer errorCount, List<String> errors, String requestId);
}
