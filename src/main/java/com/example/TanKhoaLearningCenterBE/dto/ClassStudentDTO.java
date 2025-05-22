package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import com.example.TanKhoaLearningCenterBE.entity.ClassStudentEntity;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class ClassStudentDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;

    private UUID classId;
    private String className;
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

    public ClassStudentDTO(ClassStudentEntity entity) {
        this.id = entity.getClassStudentId();
        this.studentId = entity.getStudent().getStudentId();
        this.studentName = entity.getStudent().getStdName();

        ClassEntity clazz = entity.getClazz();
        if (clazz != null) {
            this.classId = clazz.getClassId();
            this.className = clazz.getClassName();
            this.description = clazz.getDescription();

            if (clazz.getCourse() != null) {
                this.courseId = clazz.getCourse().getCourseId();
                this.courseName = clazz.getCourse().getCourseName();
            }

            if (clazz.getTeacher() != null) {
                this.teacherId = clazz.getTeacher().getTeacherId();
                this.teacherName = clazz.getTeacher().getTeacherName();
            }

            if (clazz.getDay() != null) {
                this.dayId = clazz.getDay().getDayId();
                this.dayName = clazz.getDay().getDay();
            }

            if (clazz.getTime() != null) {
                this.timeId = clazz.getTime().getTimeId();
                this.timeStart = clazz.getTime().getTimeStart();
                this.timeEnd = clazz.getTime().getTimeEnd();
            }
        }
    }
}
