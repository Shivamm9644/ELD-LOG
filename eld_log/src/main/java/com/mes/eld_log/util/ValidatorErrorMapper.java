package com.mes.eld_log.util;



import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mes.eld_log.models.EventLineIndex;

public class ValidatorErrorMapper {

    private static final Pattern LINE_PATTERN =
            Pattern.compile("(?i)line\\s*(\\d+)");

    public static String mapToEvent(String error, List<EventLineIndex> index) {

        if (error == null || index == null || index.isEmpty()) return null;

        Integer lineNo = extractLine(error);
        if (lineNo == null) return null;

        for (EventLineIndex e : index) {
            if (e.getCsvLineNumber() == lineNo) {
                return "Event → SeqId: " + n(e.getEventSeqId())
                        + ", Time: " + n(e.getEventTime())
                        + ", Type: " + n(e.getEventType())
                        + ", CSV line: " + lineNo;
            }
        }
        return null;
    }

    private static Integer extractLine(String s) {
        Matcher m = LINE_PATTERN.matcher(s);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static String n(String s) {
        return s == null ? "" : s.trim();
    }
}
