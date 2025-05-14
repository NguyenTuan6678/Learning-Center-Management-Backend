package com.example.TanKhoaLearningCenterBE.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidAccountRoleException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(InvalidAccountRoleException.class);

    public InvalidAccountRoleException() {
        super(ErrorMessages.INVALID_ACCOUNT_ROLE.getMessage());
        logger.error("***Exception {} thrown.", getClass());
    }
}
