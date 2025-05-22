package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {
    List<TeacherEntity> findByTeacherNameContainingIgnoreCase(String name);

    Optional<TeacherEntity> findByAccountIds_AccountId(UUID accountId);

    boolean existsByAccountIds_AccountId(UUID accountId);
}