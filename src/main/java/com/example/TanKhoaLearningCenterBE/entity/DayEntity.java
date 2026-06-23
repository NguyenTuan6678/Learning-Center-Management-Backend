package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Document(collection = "days")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class DayEntity extends AuditEntity {
    @Id
    private UUID dayId = UUID.randomUUID();

    
    private String day;
}
