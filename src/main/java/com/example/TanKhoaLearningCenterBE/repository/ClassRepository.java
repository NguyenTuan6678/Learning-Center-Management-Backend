package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {
    List<ClassEntity> findByClassNameContainingIgnoreCase(String name);
    List<ClassEntity> findByCourse_CourseId(UUID courseId);
    List<ClassEntity> findByTeacher_TeacherId(UUID teacherId);
}
