package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassRepository extends MongoRepository<ClassEntity, UUID> {
    @Query("{}")
    Page<ClassEntity> findAllWithDetails(Pageable pageable);
    List<ClassEntity> findByClassNameContainingIgnoreCase(String name);
    @Query("{'course.$id': ?0}")
    List<ClassEntity> findByCourse_CourseId(UUID courseId);
    @Query("{'teacher.$id': ?0}")
    List<ClassEntity> findByTeacher_TeacherId(UUID teacherId);
}
