package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class ClassDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID courseId;
    private String courseName;
    private UUID teacherId;
    private String teacherName;
    private UUID dayId;
    private String dayName;
    private UUID timeId;
    private LocalTime timeStart;
    private LocalTime timeEnd;


    public ClassDTO(ClassEntity classes) {
        this.id = classes.getClassId();
        this.name = classes.getClassName();
        this.description = classes.getDescription();
        if (classes.getCourse() != null) {
            this.courseId = classes.getCourse().getCourseId();
            this.courseName = classes.getCourse().getCourseName();
        }
        if (classes.getTeacher() != null) {
            this.teacherId = classes.getTeacher().getTeacherId();
            this.teacherName = classes.getTeacher().getTeacherName();
        }
        if (classes.getDay() != null) {
            this.dayId = classes.getDay().getDayId();
            this.dayName = classes.getDay().getDay();
        }
        if (classes.getTime() != null) {
            this.timeId = classes.getTime().getTimeId();
            this.timeStart = classes.getTime().getTimeStart();
            this.timeEnd = classes.getTime().getTimeEnd();
        }
    }
}
