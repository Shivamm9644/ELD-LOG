package com.mes.eld_log.serviceImpl;

import com.mes.eld_log.service.GeminiAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GeminiAiServiceImpl implements GeminiAiService {

    @Value("${gemini.apiKey}") private String apiKey;
    @Value("${gemini.model}") private String model;

    private final RestTemplate restTemplate;

    @Override
    public String explain(String status, Integer errorCount, List<String> errors,
                          List<String> mappedEvents, String requestId) {

        // Basic validation
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return fallback(status, errorCount, errors, requestId, "Gemini API key missing");
        }
        if (model == null || model.trim().isEmpty()) {
            model = "gemini-1.5-flash";
        }

        String prompt = buildPrompt(status, errorCount, errors, mappedEvents, requestId);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model + ":generateContent?key=" + apiKey;

        Map<String, Object> body = buildRequestBody(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> res = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            String text = extractText(res.getBody());
            if (text == null || text.trim().isEmpty()) {
                return fallback(status, errorCount, errors, requestId, "Empty Gemini response");
            }
            return text.trim();

        } catch (Exception e) {
            return fallback(status, errorCount, errors, requestId, e.getMessage());
        }
    }

    private String buildPrompt(String status, Integer errorCount, List<String> errors,
                               List<String> mappedEvents, String requestId) {

        int ec = (errorCount == null) ? 0 : errorCount;

        List<String> topErrors = limitList(errors, 3);
        List<String> topMapped = limitList(mappedEvents, 2);

        return
            "You are an FMCSA ELD compliance expert.\n" +
            "Write a short WhatsApp-style reply (max 12 lines).\n" +
            "Tone: friendly, direct, practical.\n\n" +
            "Include:\n" +
            "1) Result (PASS/FAIL/ERROR)\n" +
            "2) Top issues (max 3)\n" +
            "3) Related event (if available)\n" +
            "4) Exact next steps to fix\n\n" +
            "requestId: " + safe(requestId) + "\n" +
            "status: " + safe(status) + "\n" +
            "errorCount: " + ec + "\n" +
            "errors: " + String.valueOf(topErrors) + "\n" +
            "mappedEvents: " + String.valueOf(topMapped) + "\n";
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        // No Map.of => Java 8 compatible
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        List<Object> parts = new ArrayList<>();
        parts.add(textPart);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", parts);

        List<Object> contents = new ArrayList<>();
        contents.add(content);

        Map<String, Object> genCfg = new HashMap<>();
        genCfg.put("temperature", 0.2);
        genCfg.put("maxOutputTokens", 400);

        Map<String, Object> body = new HashMap<>();
        body.put("contents", contents);
        body.put("generationConfig", genCfg);

        return body;
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map body) {
        if (body == null) return null;

        try {
            Object candidatesObj = body.get("candidates");
            if (!(candidatesObj instanceof List)) return null;

            List candidates = (List) candidatesObj;
            if (candidates.isEmpty()) return null;

            Object c0Obj = candidates.get(0);
            if (!(c0Obj instanceof Map)) return null;

            Map c0 = (Map) c0Obj;
            Object contentObj = c0.get("content");
            if (!(contentObj instanceof Map)) return null;

            Map content = (Map) contentObj;
            Object partsObj = content.get("parts");
            if (!(partsObj instanceof List)) return null;

            List parts = (List) partsObj;
            if (parts.isEmpty()) return null;

            // Join all text parts if multiple
            StringBuilder sb = new StringBuilder();
            for (Object p : parts) {
                if (p instanceof Map) {
                    Object t = ((Map) p).get("text");
                    if (t != null) {
                        if (sb.length() > 0) sb.append("\n");
                        sb.append(String.valueOf(t));
                    }
                }
            }
            return sb.length() == 0 ? null : sb.toString();

        } catch (Exception e) {
            return null;
        }
    }

    private String fallback(String status, Integer ec, List<String> errors, String req, String reason) {
        int count = (ec == null) ? 0 : ec;
        String first = (errors == null || errors.isEmpty()) ? "" : errors.get(0);

        String msg =
            "Status: " + safe(status) + "\n" +
            "Errors: " + count + "\n";

        if (!first.trim().isEmpty()) {
            msg += "Top issue: " + shorten(first, 220) + "\n";
        }
        if (reason != null && !reason.trim().isEmpty()) {
            msg += "Note: " + shorten(reason, 220) + "\n";
        }
        msg += "Ref: " + safe(req);

        return msg;
    }

    private List<String> limitList(List<String> in, int max) {
        if (in == null || in.isEmpty()) return Collections.emptyList();
        List<String> out = new ArrayList<>();
        for (String s : in) {
            if (s == null) continue;
            if (out.size() >= max) break;
            out.add(shorten(s.trim(), 240));
        }
        return out;
    }

    private String shorten(String s, int max) {
        if (s == null) return "";
        String x = s.replaceAll("\\s+", " ").trim();
        return x.length() <= max ? x : x.substring(0, max) + "...";
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
