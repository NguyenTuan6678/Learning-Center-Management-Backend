package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.UUID;

@Document(collection = "timeTables")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class TimeTableEntity extends AuditEntity {
    @Id
    private UUID timeTableId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    private DayEntity dayIds;

    @DBRef(lazy = true)
    
    private TimeEntity timeIds;

    @DBRef(lazy = true)
    
    private ClassEntity clazz;

    @DBRef(lazy = true)
    
    private CourseEntity course;
}
