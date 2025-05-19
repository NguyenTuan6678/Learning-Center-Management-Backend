package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.ClassStudentEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class ClassStudentDTO {
    private UUID id;
    private UUID classId;
    private String className;
    private UUID studentId;
    private String studentName;

    public ClassStudentDTO(ClassStudentEntity classStudentEntity) {
        this.id = classStudentEntity.getClassStudentId();
        this.classId = classStudentEntity.getClazz().getClassId();
        this.className = classStudentEntity.getClazz().getClassName();
        this.studentId = classStudentEntity.getStudent().getStudentId();
        this.studentName = classStudentEntity.getStudent().getStdName();
    }
}
