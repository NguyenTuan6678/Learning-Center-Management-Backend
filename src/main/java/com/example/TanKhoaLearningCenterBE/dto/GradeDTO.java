package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.GradeEntity;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class GradeDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private UUID courseId;
    private String courseName;
    private UUID teacherId;
    private String teacherName;

    private Float attendanceScore;
    private Float attitudeScore;
    private Float midtermScore;
    private Float finalScore;

    private String notes;

    private Instant createdAt;
    private Instant updatedAt;

    public GradeDTO(GradeEntity entity) {
        this.id = entity.getGradeId();

        this.studentId = entity.getStudent().getStudentId();
        this.studentName = entity.getStudentName();

        this.courseId = entity.getCourse().getCourseId();
        this.courseName = entity.getCourseName();

        this.teacherId = entity.getTeacher().getTeacherId();
        this.teacherName = entity.getTeacherName();

        this.attendanceScore = entity.getAttendanceScore();
        this.attitudeScore = entity.getAttitudeScore();
        this.midtermScore = entity.getMidtermScore();
        this.finalScore = entity.getFinalScore();

        this.notes = entity.getNotes();

        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
