package com.example.TanKhoaLearningCenterBE.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AccountAlreadyAssignedException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(AccountAlreadyAssignedException.class);

    public AccountAlreadyAssignedException() {
        super(ErrorMessages.ACCOUNT_ALREADY_ASSIGNED.getMessage());
        logger.error("***Exception {} thrown.", getClass());
    }
}
