package com.mes.eld_log.serviceImpl;

import com.mes.eld_log.service.GeminiAiService;
import com.mes.eld_log.service.LlmExplainService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class LlmExplainServiceImpl implements LlmExplainService {

    private final GeminiAiService geminiAiService;

    @Override
    public String makeHumanReply(String status, Integer errorCount,
                                 List<String> errors, String requestId) {

        return geminiAiService.explain(
                status,
                errorCount,
                errors,
                List.of(),
                requestId
        );
    }
}
