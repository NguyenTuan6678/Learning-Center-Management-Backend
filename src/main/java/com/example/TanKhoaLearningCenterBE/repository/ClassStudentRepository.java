package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import com.example.TanKhoaLearningCenterBE.entity.ClassStudentEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudentEntity, UUID> {
    List<ClassStudentEntity> findByClazz_ClassId(UUID classId);
    List<ClassStudentEntity> findByStudent_StudentId(UUID studentId);
    boolean existsByStudentAndClazz(StudentEntity student, ClassEntity clazz);
}
