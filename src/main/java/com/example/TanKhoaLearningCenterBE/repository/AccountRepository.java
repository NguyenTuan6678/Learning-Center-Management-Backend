package com.example.TanKhoaLearningCenterBE.repository;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByUserNameContainingIgnoreCase(String username);

    List<AccountEntity> findByRole(Role role);

    boolean existsByRole(Role role);
}
