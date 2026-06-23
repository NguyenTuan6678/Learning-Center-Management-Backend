package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends MongoRepository<TeacherEntity, UUID> {
    List<TeacherEntity> findByTeacherNameContainingIgnoreCase(String name);

    @Query("{'accountIds.$id': ?0}")
    Optional<TeacherEntity> findByAccountIds_AccountId(UUID accountId);

    @Query(value = "{'accountIds.$id': ?0}", exists = true)
    boolean existsByAccountIds_AccountId(UUID accountId);
}