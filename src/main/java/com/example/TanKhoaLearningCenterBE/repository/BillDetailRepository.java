package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillDetailRepository extends MongoRepository<BillDetailEntity, UUID> {
    @Query("{'parent.$id': ?0}")
    List<BillDetailEntity> findAllByParent_ParentId(UUID parentId);
    @Query("{'student.$id': ?0}")
    List<BillDetailEntity> findAllByStudent_StudentId(UUID studentId);
}
