package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ReviewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewEntity, UUID> {
    List<ReviewEntity> findByStudentIdsIn(List<StudentEntity> students);
}
