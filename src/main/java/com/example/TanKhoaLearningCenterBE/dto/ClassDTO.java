package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import lombok.Data;

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
    }
}
