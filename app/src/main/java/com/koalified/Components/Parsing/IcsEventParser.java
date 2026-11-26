package com.koalified.Components.Parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class IcsEventParser {
    private IcsEventParser() {
    }

    public static List<CalendarEvent> parse(InputStream input) throws IOException {
        List<CalendarEvent> events = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8));

        StringBuilder logicalLine = new StringBuilder();
        String physicalLine;
        Map<String, String> properties = null;

        while ((physicalLine = reader.readLine()) != null) {
            // iCalendar continuation lines start with a space or tab.
            if (physicalLine.startsWith(" ") || physicalLine.startsWith("\t")) {
                if (logicalLine.length() > 0) {
                    logicalLine.append(physicalLine.substring(1));
                }
                continue;
            }

            if (logicalLine.length() > 0) {
                properties = processLine(logicalLine.toString(), properties, events);
                logicalLine.setLength(0);
            }
            logicalLine.append(physicalLine);
        }

        if (logicalLine.length() > 0) {
            processLine(logicalLine.toString(), properties, events);
        }

        return events;
    }

    private static Map<String, String> processLine(
            String line,
            Map<String, String> currentEvent,
            List<CalendarEvent> events) throws IOException {

        if ("BEGIN:VEVENT".equalsIgnoreCase(line)) {
            return new HashMap<>();
        }

        if ("END:VEVENT".equalsIgnoreCase(line)) {
            if (currentEvent != null) {
                events.add(toEvent(currentEvent));
            }
            return null;
        }

        if (currentEvent == null) {
            return null; // Ignore calendar-level properties, such as VTIMEZONE.
        }

        int colon = line.indexOf(':');
        if (colon < 0) {
            return currentEvent;
        }

        String propertyAndParams = line.substring(0, colon);
        String value = line.substring(colon + 1);
        String[] parts = propertyAndParams.split(";", -1);
        String propertyName = parts[0].toUpperCase(Locale.ROOT);

        String timeZoneId = null;
        for (int i = 1; i < parts.length; i++) {
            int equals = parts[i].indexOf('=');
            if (equals > 0
                    && "TZID".equalsIgnoreCase(parts[i].substring(0, equals))) {
                timeZoneId = parts[i].substring(equals + 1).replace("\"", "");
            }
        }

        if ("DTSTART".equals(propertyName) || "DTEND".equals(propertyName)) {
            currentEvent.put(propertyName, value);
            if (timeZoneId != null) {
                currentEvent.put(propertyName + "_TZID", timeZoneId);
            }
        } else {
            currentEvent.put(propertyName, unescapeText(value));
        }

        return currentEvent;
    }

    private static CalendarEvent toEvent(Map<String, String> properties)
            throws IOException {
        String startTimeZone = properties.get("DTSTART_TZID");
        String endTimeZone = properties.get("DTEND_TZID");
        String timeZoneId = startTimeZone != null ? startTimeZone : endTimeZone;

        Date start = parseDate(properties.get("DTSTART"), startTimeZone);
        Date end = parseDate(properties.get("DTEND"), endTimeZone);

        return new CalendarEvent(
                properties.get("UID"),
                properties.get("SUMMARY"),
                properties.get("LOCATION"),
                properties.get("DESCRIPTION"),
                start,
                end,
                timeZoneId);
    }

    private static String unescapeText(String value) {
        return value
                .replace("\\n", "\n")
                .replace("\\N", "\n")
                .replace("\\,", ",")
                .replace("\\;", ";")
                .replace("\\\\", "\\");
    }

    private static Date parseDate(String value, String timeZoneId)
            throws IOException {
        if (value == null) {
            return null;
        }

        boolean utc = value.endsWith("Z");
        String dateValue = utc ? value.substring(0, value.length() - 1) : value;

        String[] formats = {
                "yyyyMMdd'T'HHmmss",
                "yyyyMMdd'T'HHmm",
                "yyyyMMdd"
        };

        for (String format : formats) {
            SimpleDateFormat parser = new SimpleDateFormat(format, Locale.ROOT);
            parser.setLenient(false);

            if (utc) {
                parser.setTimeZone(TimeZone.getTimeZone("UTC"));
            } else if (timeZoneId != null) {
                parser.setTimeZone(TimeZone.getTimeZone(timeZoneId));
            }

            try {
                return parser.parse(dateValue);
            } catch (ParseException ignored) {
                // Try the next supported iCalendar date/time format.
            }
        }

        throw new IOException("Unsupported iCalendar date/time value: " + value);
    }

    public static final class CalendarEvent {
        public final String uid;
        public final String summary;
        public final String location;
        public final String description;
        public final Date start;
        public final Date end;
        public final String timeZoneId;

        private CalendarEvent(
                String uid,
                String summary,
                String location,
                String description,
                Date start,
                Date end,
                String timeZoneId) {
            this.uid = uid;
            this.summary = summary;
            this.location = location;
            this.description = description;
            this.start = start;
            this.end = end;
            this.timeZoneId = timeZoneId;
        }
    }
}