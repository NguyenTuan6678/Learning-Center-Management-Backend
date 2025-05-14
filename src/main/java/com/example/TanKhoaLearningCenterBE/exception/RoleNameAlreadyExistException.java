package com.example.TanKhoaLearningCenterBE.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoleNameAlreadyExistException extends RuntimeException {
  private static final Logger logger = LoggerFactory.getLogger(RoleNameAlreadyExistException.class);

  public RoleNameAlreadyExistException(){
    super(ErrorMessages.ROLENAME_ALREADY_EXIST.getMessage());
    logger.error("***Exception {} thrown.", getClass());
  }
}
