package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.CourseEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseDTO {
    private UUID courseId;
    private String courseName;
    private String description;
    private Integer numberOfClasses;

    public CourseDTO(CourseEntity courseEntity) {
        this.courseId = courseEntity.getCourseId();
        this.courseName = courseEntity.getCourseName();
        this.description = courseEntity.getDescription();
        if (courseEntity.getClasses() != null) {
            this.numberOfClasses = courseEntity.getClasses().size();
        } else {
            this.numberOfClasses = 0;
        }
    }
}
