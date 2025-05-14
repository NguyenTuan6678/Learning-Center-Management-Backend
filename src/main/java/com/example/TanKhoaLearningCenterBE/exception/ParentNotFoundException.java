package com.example.TanKhoaLearningCenterBE.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ParentNotFoundException.class);

    public ParentNotFoundException() {
        super(ErrorMessages.PARENT_NOT_FOUND.getMessage());
        logger.error("***Exception {} thrown.", getClass());
    }
}
