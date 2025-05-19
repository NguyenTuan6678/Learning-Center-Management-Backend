package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ClassDTO;
import com.example.TanKhoaLearningCenterBE.entity.*;
import com.example.TanKhoaLearningCenterBE.repository.*;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateClassRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateClassRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TimeRepository timeRepository;
    private final DayRepository dayRepository;

    @Override
    public ResponseEntity<ClassDTO> createClass(CreateClassRequest request) {
        ClassEntity clazz = new ClassEntity();
        clazz.setClassName(request.getClassName());
        clazz.setDescription(request.getDescription());

        CourseEntity course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Cant not found course ID: " + request.getCourseId()));
        clazz.setCourse(course);

        TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Cant not found teacher ID: " + request.getTeacherId()));
        clazz.setTeacher(teacher);

        ClassEntity savedClass = classRepository.save(clazz);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClassDTO(savedClass));
    }

    @Override
    public ResponseEntity<ClassDTO> updateClass(UUID id, UpdateClassRequest request) {
        Optional<ClassEntity> optionalClass = classRepository.findById(id);
        if (optionalClass.isPresent()) {
            ClassEntity clazz = optionalClass.get();
            if (request.getClassName() != null && !request.getClassName().isBlank()) {
                clazz.setClassName(request.getClassName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                clazz.setDescription(request.getDescription());
            }
            if (request.getCourseId() != null) {
                CourseEntity course = courseRepository.findById(request.getCourseId())
                        .orElseThrow(() -> new RuntimeException("Cant not found course ID: " + request.getCourseId()));
                clazz.setCourse(course);
            }
            if (request.getTeacherId() != null) {
                TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                        .orElseThrow(() -> new RuntimeException("Cant not found teacher ID: " + request.getTeacherId()));
                clazz.setTeacher(teacher);
            }

            if (request.getDayId() != null) {
                DayEntity day = dayRepository.findById(request.getDayId())
                        .orElseThrow(() -> new RuntimeException("Day not found: " + request.getDayId()));
                clazz.setDay(day);
            }
            if (request.getTimeId() != null) {
                TimeEntity time = timeRepository.findById(request.getTimeId())
                        .orElseThrow(() -> new RuntimeException("Time not found: " + request.getTimeId()));
                clazz.setTime(time);
            }
            ClassEntity updatedClass = classRepository.save(clazz);
            return ResponseEntity.ok(new ClassDTO(updatedClass));
        }
        throw new RuntimeException("Class not found");
    }

    @Override
    public ResponseEntity<?> deleteClass(UUID id) {
        Optional<ClassEntity> optionalClass = classRepository.findById(id);
        if (optionalClass.isPresent()) {
            classRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new RuntimeException("Class not found");
    }

    @Override
    public ResponseEntity<ClassDTO> getClassById(UUID id) {
        Optional<ClassEntity> optionalClass = classRepository.findById(id);
        if (optionalClass.isPresent()) {
            return ResponseEntity.ok(new ClassDTO(optionalClass.get()));
        }
        throw new RuntimeException("Class not found");
    }

    @Override
    public ResponseEntity<List<ClassDTO>> searchClassByName(String name) {
        List<ClassEntity> classes = classRepository.findByClassNameContainingIgnoreCase(name);
        if (!classes.isEmpty()) {
            return ResponseEntity.ok(classes.stream().map(ClassDTO::new).collect(Collectors.toList()));
        }
        throw new RuntimeException("Class not found");
    }

    @Override
    public ResponseEntity<PageResponse<ClassDTO>> getAllClasses(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassEntity> classPage = classRepository.findAll(pageable);
        List<ClassDTO> rows = classPage.getContent().stream().map(ClassDTO::new).collect(Collectors.toList());

        PageResponse<ClassDTO> response = new PageResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setCount(classPage.getTotalElements());
        response.setRows(rows);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ClassDTO>> getClassesByCourse(UUID courseId) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            List<ClassEntity> classes = classRepository.findByCourse_CourseId(courseId);
            return ResponseEntity.ok(classes.stream().map(ClassDTO::new).collect(Collectors.toList()));
        }
        throw new RuntimeException("Course not found");
    }

    @Override
    public ResponseEntity<List<ClassDTO>> getClassesByTeacher(UUID teacherId) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(teacherId);
        if (optionalTeacher.isPresent()) {
            List<ClassEntity> classes = classRepository.findByTeacher_TeacherId(teacherId);
            return ResponseEntity.ok(classes.stream().map(ClassDTO::new).collect(Collectors.toList()));
        }
        throw new RuntimeException("Teacher not found");
    }
}
