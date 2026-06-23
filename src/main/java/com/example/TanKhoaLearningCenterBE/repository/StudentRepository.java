package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends MongoRepository<StudentEntity, UUID> {
    @Query("{'accountIds.$id': ?0}")
    Optional<StudentEntity> findByAccountIds_AccountId(UUID accountId);

    List<StudentEntity> findByStdNameContainingIgnoreCase(String name);

    @Query(value = "{'accountIds.$id': ?0}", exists = true)
    boolean existsByAccountIds_AccountId(UUID accountId);

//    @Query(value = "SELECT p FROM StudentEntity p WHERE 1=1 and p.stdName LIKE %:keyword%")
//    List<StudentEntity> findByNameCustom(@Param("keyword") String name);
}
