package com.dynamis.validators;

public class StudentIdValidator implements Validator {

    @Override
    public boolean isValid(String studentId) {
        return studentId.matches("^[A-Za-z]{2}/\\d{4}/\\d{3}$");
    }
}
