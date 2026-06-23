package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.ParentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentRepository extends MongoRepository<ParentEntity, UUID> {
    List<ParentEntity> findByParentNameContainingIgnoreCase(String name);

    @Query(value = "{'accountIds.$id': ?0}", exists = true)
    boolean existsByAccountIds_AccountId(UUID accountId);
}
