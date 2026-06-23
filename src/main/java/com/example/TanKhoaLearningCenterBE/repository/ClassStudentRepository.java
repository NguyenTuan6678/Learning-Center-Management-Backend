package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ClassEntity;
import com.example.TanKhoaLearningCenterBE.entity.ClassStudentEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassStudentRepository extends MongoRepository<ClassStudentEntity, UUID> {
    @Query("{'clazz.$id': ?0}")
    List<ClassStudentEntity> findByClazz_ClassId(UUID classId);
    @Query("{'student.$id': ?0}")
    List<ClassStudentEntity> findByStudent_StudentId(UUID studentId);
    boolean existsByStudentAndClazz(StudentEntity student, ClassEntity clazz);
}
