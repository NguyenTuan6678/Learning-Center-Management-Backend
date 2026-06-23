package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, UUID> {
    @Query("{'bill.$id': ?0}")
    Optional<PaymentEntity> findByBill_BillId(UUID billId);

    @Query(value = "{'bill.$id': ?0}", exists = true)
    boolean existsByBill_BillId(UUID billId);
}
