package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.CourseEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseDTO {
    private UUID id;
    private String name;
    private String description;

    public CourseDTO(CourseEntity courseEntity) {
        this.id = courseEntity.getCourseId();
        this.name = courseEntity.getCourseName();
        this.description = courseEntity.getDescription();
    }
}
