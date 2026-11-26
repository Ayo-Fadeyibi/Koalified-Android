package com.koalified.Components.Enums;

public enum SessionType {

    LECTURE("LEC"),
    TUTORIAL("TUT"),
    PRACTICAL("PRAC)"),
    STUDIO("STU"),
    APPLIED("APP"),
    WORKSHOP("WKS"),
    CLINICAL_TRAINING("CLN"),
    SEMINAR("SEM"),
    EXAM("EXM"),
    RECORDED("REC"),
    PLACEMENT("PLM"),
    FIELDWORK("FLD"),
    SYMPOSIUM("SYM"),
    CONSULTATION("CON"),
    EVENT("EVT"),
    IT_COMPUTING("COM"),
    PEER_ASSISTED_STUDY_SESSION("PAS");

    private final String rawValue;

    SessionType(String rawValue) {
        this.rawValue = rawValue;
    }

    public static SessionType sessionFromRawValue(String rawValue) {
        if (rawValue == null) {
            return null;
        }

        for (SessionType type : values()) {
            if (type.rawValue.equalsIgnoreCase(rawValue)
                    || type.name().equalsIgnoreCase(rawValue)) {
                return type;
            }
        }
        return null;
    }

    public static String localilsedSession(SessionType sessionType) {
        if (sessionType == null) {
            return null;
        }
        switch (sessionType) {
            case LECTURE:
                return "Lecture";
            case TUTORIAL:
                return "Tutorial";
            case PRACTICAL:
                return "Practical";
            case STUDIO:
                return "Studio";
            case APPLIED:
                return "Applied Class";
            case WORKSHOP:
                return "Workshop";
            case CLINICAL_TRAINING:
                return "Clinical Training";
            case SEMINAR:
                return "Seminar";
            case EXAM:
                return "Exam";
            case RECORDED:
                return "Recorded";
            case PLACEMENT:
                return "Placement";
            case FIELDWORK:
                return "Fieldwork";
            case SYMPOSIUM:
                return "Symposium";
            case CONSULTATION:
                return "Consultation";
            case EVENT:
                return "Event";
            case IT_COMPUTING:
                return "IT Computing";
            case PEER_ASSISTED_STUDY_SESSION:
                return "Peer Assisted Study Session (PASS)";
            default:
                return "unknown";
        }
    }
}