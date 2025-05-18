package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.AttendanceEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class AttendanceDTO {
    private UUID id;
    private UUID studentId;
    private UUID classId;
    private String status;

    public AttendanceDTO(AttendanceEntity attendance) {
        this.id = attendance.getAttendId();
        if (attendance.getStudent() != null) {
            this.studentId = attendance.getStudent().getStudentId();

        }
        if (attendance.getClazz() != null) {
            this.classId = attendance.getClazz().getClassId();
        }
        if (attendance.getStatus() != null) {
            this.status =  attendance.getStatus().toString();
        }
    }
}
