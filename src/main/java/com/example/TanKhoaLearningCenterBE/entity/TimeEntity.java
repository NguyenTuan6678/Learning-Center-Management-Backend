package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Document(collection = "times")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class TimeEntity extends AuditEntity {
    @Id
    private UUID timeId = UUID.randomUUID();

    
    private LocalTime timeStart;

    
    private LocalTime timeEnd;
}
