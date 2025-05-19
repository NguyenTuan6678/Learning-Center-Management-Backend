package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
    Optional<StudentEntity> findByAccountIds_AccountId(UUID accountId);

    List<StudentEntity> findByStdNameContainingIgnoreCase(String name);

    boolean existsByAccountIds_AccountId(UUID accountId);

//    @Query(value = "SELECT p FROM StudentEntity p WHERE 1=1 and p.stdName LIKE %:keyword%")
//    List<StudentEntity> findByNameCustom(@Param("keyword") String name);
}
