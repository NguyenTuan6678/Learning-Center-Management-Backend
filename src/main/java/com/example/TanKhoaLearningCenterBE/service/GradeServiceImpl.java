package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.GradeDTO;
import com.example.TanKhoaLearningCenterBE.entity.CourseEntity;
import com.example.TanKhoaLearningCenterBE.entity.GradeEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import com.example.TanKhoaLearningCenterBE.repository.CourseRepository;
import com.example.TanKhoaLearningCenterBE.repository.GradeRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.repository.TeacherRepository;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateGradeRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateGradeRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @Override
    public ResponseEntity<GradeDTO> createGrade(CreateGradeRequest request) {
        GradeEntity grade = new GradeEntity();

        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        CourseEntity course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        grade.setStudent(student);
        grade.setStudentName(student.getStdName());
        grade.setCourse(course);
        grade.setCourseName(course.getCourseName());
        grade.setTeacher(teacher);
        grade.setTeacherName(teacher.getTeacherName());

        grade.setAttendanceScore(request.getAttendanceScore());
        grade.setAttitudeScore(request.getAttitudeScore());
        grade.setMidtermScore(request.getMidtermScore());
        grade.setFinalScore(request.getFinalScore());
        grade.setNotes(request.getNotes());

        var saved = gradeRepository.save(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GradeDTO(saved));
    }

    @Override
    public ResponseEntity<GradeDTO> updateGrade(UUID id, UpdateGradeRequest request) {
        GradeEntity grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        if (request.getAttendanceScore() != null) {
            grade.setAttendanceScore(request.getAttendanceScore());
        }
        if (request.getAttitudeScore() != null) {
            grade.setAttitudeScore(request.getAttitudeScore());
        }
        if (request.getMidtermScore() != null) {
            grade.setMidtermScore(request.getMidtermScore());
        }
        if (request.getFinalScore() != null) {
            grade.setFinalScore(request.getFinalScore());
        }
        if (request.getNotes() != null) {
            grade.setNotes(request.getNotes());
        }

        var updated = gradeRepository.save(grade);
        return ResponseEntity.ok(new GradeDTO(updated));
    }

    @Override
    public ResponseEntity<?> deleteGrade(UUID id) {
        if (!gradeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grade not found");
        }
        gradeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<GradeDTO>> searchGrade(String name) {
        List<StudentEntity> students = studentRepository.findByStdNameContainingIgnoreCase(name);
        List<GradeEntity> results = gradeRepository.findByStudentIn(students);
        List<GradeDTO> dtoList = results.stream().map(GradeDTO::new).toList();
        return ResponseEntity.ok(dtoList);
    }

    @Override
    public ResponseEntity<PageResponse<GradeDTO>> getAllGrade(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GradeEntity> pageResult = gradeRepository.findAll(pageable);

        List<GradeDTO> rows = pageResult.getContent().stream().map(GradeDTO::new).toList();

        PageResponse<GradeDTO> response = new PageResponse<>();
        response.setCount(pageResult.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.ok(response);
    }
}
