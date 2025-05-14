package com.example.TanKhoaLearningCenterBE.exception;

public enum ErrorMessages {
    ACCOUNT_NOT_FOUND("Account not found"),
    ACCOUNT_ALREADY_ASSIGNED("Account not found"),
    USERNAME_ALREADY_EXIST("Username already exist, pick another one"),
    ROLENAME_ALREADY_EXIST("Rolename already exist"),
    STUDENT_NOT_FOUND("Student not found"),
    PARENT_NOT_FOUND("Parent not found"),
    INVALID_ACCOUNT_ROLE("Account must have role to be assigned to that role."),
    FORBIDDEN("User is forbidden!");

    private final String message;

    ErrorMessages(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
