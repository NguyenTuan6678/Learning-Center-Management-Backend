package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ClassStudentDTO;
import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import com.example.TanKhoaLearningCenterBE.entity.ClassStudentEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.exception.StudentNotFoundException;
import com.example.TanKhoaLearningCenterBE.repository.ClassRepository;
import com.example.TanKhoaLearningCenterBE.repository.ClassStudentRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.controller.request.AddStudentToClassRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
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
public class ClassStudentServiceImpl implements ClassStudentService {
    private final ClassStudentRepository classStudentRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<ClassStudentDTO> addStudentToClass(AddStudentToClassRequest request) {
        ClassEntity clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Cant not found course ID: " + request.getClassId()));
        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(StudentNotFoundException::new);

        ClassStudentEntity classStudent = new ClassStudentEntity();
        classStudent.setClazz(clazz);
        classStudent.setStudent(student);

        ClassStudentEntity savedClassStudent = classStudentRepository.save(classStudent);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClassStudentDTO(savedClassStudent));
    }

    @Override
    public ResponseEntity<?> removeStudentFromClass(UUID classStudentId) {
        Optional<ClassStudentEntity> optionalClassStudent = classStudentRepository.findById(classStudentId);
        if (optionalClassStudent.isPresent()) {
            classStudentRepository.deleteById(classStudentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new RuntimeException("ClassStudent not found");
    }

    @Override
    public ResponseEntity<ClassStudentDTO> getClassStudentById(UUID id) {
        Optional<ClassStudentEntity> optionalClassStudent = classStudentRepository.findById(id);
        if (optionalClassStudent.isPresent()) {
            return ResponseEntity.ok(new ClassStudentDTO(optionalClassStudent.get()));
        }
        throw new RuntimeException("ClassStudent not found");
    }

    @Override
    public ResponseEntity<PageResponse<ClassStudentDTO>> getAllClassStudents(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassStudentEntity> classStudentPage = classStudentRepository.findAll(pageable);
        List<ClassStudentDTO> rows = classStudentPage.getContent().stream().map(ClassStudentDTO::new).collect(Collectors.toList());

        PageResponse<ClassStudentDTO> response = new PageResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setCount(classStudentPage.getTotalElements());
        response.setRows(rows);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ClassStudentDTO>> getClassStudentsByClass(UUID classId) {
        Optional<ClassEntity> optionalClass = classRepository.findById(classId);
        if (optionalClass.isPresent()) {
            List<ClassStudentEntity> classStudents = classStudentRepository.findByClazz_ClassId(classId);
            return ResponseEntity.ok(classStudents.stream().map(ClassStudentDTO::new).collect(Collectors.toList()));
        }
        throw new RuntimeException("Class not found");
    }

    @Override
    public ResponseEntity<List<ClassStudentDTO>> getClassStudentsByStudent(UUID studentId) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            List<ClassStudentEntity> classStudents = classStudentRepository.findByStudent_StudentId(studentId);
            return ResponseEntity.ok(classStudents.stream().map(ClassStudentDTO::new).collect(Collectors.toList()));
        }
        throw new StudentNotFoundException();
    }
}
