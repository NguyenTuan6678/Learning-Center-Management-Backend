package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, UUID> {
    List<ParentEntity> findByParentNameContainingIgnoreCase(String name);

    boolean existsByAccountIds_AccountId(UUID accountId);
}
