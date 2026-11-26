package com.koalified.Models;

import com.koalified.Components.Enums.SessionType;

import java.time.Instant;

public class ClassEvent {
    public String icsUID;
    public String typeCode;
    public Instant startTime;
    public Instant endTime;
    public SessionType sessionType;
    public String room;
    public String building;
    public String courseCode;
    public boolean isClashable;
    // In person or online
    // TODO: Find the different types of modes, in person, online, etc
    public String mode;
    public boolean isRecorded;

    public ClassEvent(String icsUID, String typeCode, Instant startTime, Instant endTime, SessionType sessionType, String room, String building, Course course, boolean isClashable, String mode, Boolean isRecorded) {
        this.icsUID = icsUID;
        this.typeCode = typeCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionType = sessionType;
        this.room = room;
        this.building = building;
        this.courseCode = course.courseCode;
        this.isClashable = isClashable;
        this.mode = mode;
        this.isRecorded = isRecorded;
    }

    public ClassEvent() {
    }
}