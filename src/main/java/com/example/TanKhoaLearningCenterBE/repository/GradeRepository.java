package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.GradeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GradeRepository extends MongoRepository<GradeEntity, UUID> {
    List<GradeEntity> findByStudentIn(List<StudentEntity> students);
}
