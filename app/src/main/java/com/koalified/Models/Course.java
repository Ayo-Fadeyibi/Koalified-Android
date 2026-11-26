package com.koalified.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.koalified.Components.Enums.Semester;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String courseCode;
    public String courseTitle;

    public Semester semester;
    public int year;

    public String staffName;

    public Course(
            String courseCode,
            String courseTitle,
            Semester semester,
            int year
    ) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.semester = semester;
        this.year = year;
    }

//    public static Course courseFromCode(String courseCode) {
//        if (courseCode == null) {
//            return null;
//        }
//        for (Course course : courses) {
//            if (course.courseCode.equals(courseCode)) {
//                return course;
//            }
//        }
//        return null;
//    }

    public String getSemesterLabel() {
        return semester.name() + ", " + year;
    }
}