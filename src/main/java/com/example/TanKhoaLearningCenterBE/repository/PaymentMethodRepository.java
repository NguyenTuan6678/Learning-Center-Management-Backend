package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.PaymentMethodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends MongoRepository<PaymentMethodEntity, UUID> {
    Optional<PaymentMethodEntity> findByPayMethod(String payMethod);

    boolean existsByPayMethod(String code);
}
