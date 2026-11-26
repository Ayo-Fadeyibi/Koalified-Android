package com.koalified.Components.Enums;

import java.util.Objects;

public enum Semester {
    S1("S1", 1),
    S2("S2", 2),
    SEM1("Sem1", 3),
    SEM2("Sem2", 4),
    SUMMER("Summer", 5);

    private final String rawValue;
    private final int sortOrder;

    Semester(String rawValue, int sortOrder) {
        this.rawValue = rawValue;
        this.sortOrder = sortOrder;
    }

    public static Semester semesterFromRawValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        for (Semester semester : values()) {
            if (semester.rawValue.equalsIgnoreCase(normalized)) {
                return semester;
            }
        }
        return null;
    }

    public String rawValue() {
        return rawValue;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public record SemesterKey(Semester semester, Integer year) {
        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof SemesterKey that)) return false;
            return Objects.equals(semester, that.semester)
                    && Objects.equals(year, that.year);
        }

        @Override
        public int hashCode() {
            return Objects.hash(semester, year);
        }
    }

    public record DetectedSemester(
            Semester semester,
            Integer year,
            int courseCount,
            int classCount
    ) {
        public String id() {
            return (semester == null ? "none" : semester.rawValue())
                    + "-"
                    + (year == null ? "none" : year);
        }
    }
}