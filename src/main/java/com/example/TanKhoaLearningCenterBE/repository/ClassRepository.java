package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {
    @Query(value = "SELECT c FROM ClassEntity c LEFT JOIN FETCH c.day LEFT JOIN FETCH c.time LEFT JOIN FETCH c.course LEFT JOIN FETCH c.teacher",
            countQuery = "SELECT count(c.classId) FROM ClassEntity c")
    Page<ClassEntity> findAllWithDetails(Pageable pageable);
    List<ClassEntity> findByClassNameContainingIgnoreCase(String name);
    List<ClassEntity> findByCourse_CourseId(UUID courseId);
    List<ClassEntity> findByTeacher_TeacherId(UUID teacherId);
}
